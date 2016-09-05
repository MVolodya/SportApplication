package info.androidhive.firebase.Classes.Managers;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;
import info.androidhive.firebase.Classes.Models.RatedUser;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.RateMatch.RateMatchResponse;
import info.androidhive.firebase.Classes.Retrofit.RateMatch.RateMatchService;
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
                                List<RatedMatchesToDB> matchesToDBs = ratedUser.getRatedMatches();
                                matchesToDBs.add(DataHelper.getInstance().getDeletedPosition(),
                                        deletedRate);
                                ratedUser.setRatedMatches(matchesToDBs);
                                mDatabase.child(username).setValue(ratedUser);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        }

                );
    }

    public void deleteRate(final String username, final int position) {
        mDatabase.child(username)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                List<RatedMatchesToDB> matchesToDBs = ratedUser.getRatedMatches();
                                DataHelper.getInstance().setRatedMatchesToDB(matchesToDBs.get(position));
                                DataHelper.getInstance().setDeletedPosition(position);
                                matchesToDBs.remove(position);
                                ratedUser.setRatedMatches(matchesToDBs);
                                mDatabase.child(username).setValue(ratedUser);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        }

                );
    }

    public void getUsersRates(final String username,
                              final UserRateManager userRateManager) {
        mDatabase.child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                                        userRateManager.setUserRateList(ratedUser.getRatedMatches());
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {}
                                                }

                );

    }

    public void checkRate(final List<RatedMatchesToDB> rateList, final String name) {

        mDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                if (rateList != null) {
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkWithApi(final List<RatedMatchesToDB> rateList, final RatedUser ratedUser,
                             final int i, final String name, final String userType){
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
                    if(homeTeamGoal>awayTeamGoal) type = WIN_FIRST;
                    if(homeTeamGoal==awayTeamGoal) type = DRAW;
                    if(homeTeamGoal<awayTeamGoal) type = WIN_SECOND;

                    if (ratedUser.getRatedMatches().get(i).getStatus()
                            .equalsIgnoreCase("unchecked")) {
                        ratedUser.getRatedMatches().get(i).setStatus("checked");

                        if(type!=null && type.equalsIgnoreCase(userType)) {
                            double currentPoints = Double.parseDouble(ratedUser.getCurrentPoints());
                            double ratePoints = Double.parseDouble(rateList.get(i).getPoints());
                            ratedUser.setCurrentPoints("" + (currentPoints + ratePoints));
                            mDatabase.child(name).child("currentPoints")
                                    .setValue(ratedUser.getCurrentPoints());
                        }
                        mDatabase.child(name).child("ratedMatches")
                                .child(""+i).child("status")
                                .setValue(ratedUser.getRatedMatches().get(i).getStatus());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {}
        });
    }

}
