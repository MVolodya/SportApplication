package info.androidhive.firebase.classes.managers;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.activity.splashScreenActivity.callback.CheckRateCallback;
import info.androidhive.firebase.activity.splashScreenActivity.view.SplashScreenView;
import info.androidhive.firebase.classes.managers.swipeManager.InitDeletedRate;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


public class RateManager {

    public static final String WIN_FIRST = "W1";
    public static final String DRAW = "D";
    public static final String WIN_SECOND = "W2";

    private final Context context;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public RateManager(Context context) {
        this.context = context;
    }

    public void addPoints(final String username) {
        mDatabase.child(username)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                mDatabase.child(username).child("currentPoints")
                                        .setValue(String.valueOf(
                                                Double.parseDouble(ratedUser.getCurrentPoints())+10));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        }
                );
    }

    public void setRate(String name, String matchId, String pointsRate, String points, String typeOfRate) {
        RemoteDatabaseManager remoteDatabaseManager = new RemoteDatabaseManager(context);
        remoteDatabaseManager.setRateToDatabase(name, matchId, pointsRate, points, typeOfRate);
    }

    public void setDeletedRate(final String username, final RatedMatchesToDB deletedRate) {
        mDatabase.child(username)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                List<RatedMatchesToDB> matchesToDBs;
                                if (ratedUser.getRatedMatches() != null)
                                    matchesToDBs = ratedUser.getRatedMatches();
                                else matchesToDBs = new ArrayList<>();
                                matchesToDBs.add(deletedRate);
                                ratedUser.setRatedMatches(matchesToDBs);
                                mDatabase.child(username).setValue(ratedUser);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        }

                );
    }

    public void deleteRate(final String username, final Rate rate, final InitDeletedRate initDeletedRate) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                        List<RatedMatchesToDB> ratedMatchesToDBs = ratedUser.getRatedMatches();

                        if (ratedMatchesToDBs != null) {
                            for (RatedMatchesToDB ratedMatchesToDB : ratedMatchesToDBs) {
                                if (ratedMatchesToDB.getMatchId().equals(rate.getId())) {
                                    initDeletedRate.initRate(ratedMatchesToDB);
                                    ratedMatchesToDBs.remove(ratedMatchesToDBs.indexOf(ratedMatchesToDB));
                                    ratedUser.setRatedMatches(ratedMatchesToDBs);
                                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                            .setValue(ratedUser);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void getUsersRates(final String username,
                              final SplashScreenView splashScreenView) {
        mDatabase.child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                                        splashScreenView.setUserRateList(ratedUser.getRatedMatches());
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                }

                );

    }

    public void checkRate(final List<RatedMatchesToDB> rateList, final String name,
                          final CheckRateCallback checkRateCallback) {

        mDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                if (rateList != null && rateList.size() > 0) {
                    for (int i = 0; i < rateList.size(); i++) {
                        if (rateList.get(i).getTypeOfRate().equalsIgnoreCase(WIN_FIRST)) {
                            checkWithApi(rateList, ratedUser, i, name, WIN_FIRST);
                        }

                        if (rateList.get(i).getTypeOfRate().equalsIgnoreCase(DRAW)) {
                            checkWithApi(rateList, ratedUser, i, name, DRAW);
                        }

                        if (rateList.get(i).getTypeOfRate().equalsIgnoreCase(WIN_SECOND)) {
                            checkWithApi(rateList, ratedUser, i, name, WIN_SECOND);
                        }
                    }
                    checkRateCallback.onSuccess();
                } else checkRateCallback.onFail();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void checkWithApi(final List<RatedMatchesToDB> rateList, final RatedUser ratedUser,
                              final int i, final String name, final String userType) {
        RateMatchService service = ApiFactory.getRateMatchService();
        Call<RateMatchResponse> call = service.match(Integer.parseInt(rateList.get(i).getMatchId()));
        call.enqueue(new Callback<RateMatchResponse>() {
            @Override
            public void onResponse(Response<RateMatchResponse> response) {
                RateMatchResponse rateMatchResponse = response.body();
                if (rateMatchResponse.getFixture().getStatus().equalsIgnoreCase("FINISHED")) {
                    final int homeTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsHomeTeam();
                    final int awayTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsAwayTeam();
                    String type = null;
                    if (homeTeamGoal > awayTeamGoal) type = WIN_FIRST;
                    if (homeTeamGoal == awayTeamGoal) type = DRAW;
                    if (homeTeamGoal < awayTeamGoal) type = WIN_SECOND;

                    if (ratedUser.getRatedMatches().get(i).getStatus()
                            .equalsIgnoreCase("unchecked")) {
                        ratedUser.getRatedMatches().get(i).setStatus("lose");

                        if (type != null && type.equalsIgnoreCase(userType)) {
                            double currentPoints = Double.parseDouble(ratedUser.getCurrentPoints());
                            double ratePoints = Double.parseDouble(rateList.get(i).getPoints());
                            ratedUser.setCurrentPoints(String.format("%.1f", (currentPoints + ratePoints)));
                            ratedUser.getRatedMatches().get(i).setStatus("win");
                            mDatabase.child(name).child("currentPoints")
                                    .setValue(ratedUser.getCurrentPoints());
                        }
                        mDatabase.child(name).child("ratedMatches")
                                .child("" + i).child("status")
                                .setValue(ratedUser.getRatedMatches().get(i).getStatus());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

}
