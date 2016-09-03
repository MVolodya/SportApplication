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
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                }

                );

    }

    public void checkRate(final List<RatedMatchesToDB> rateList, final String name) {

        if (rateList != null) {
            for (int i = 0; i < rateList.size(); i++) {

                if (rateList.get(i).getTypeOfRate().equalsIgnoreCase(WIN_FIRST)) {
                    RateMatchService service = ApiFactory.getRateMatchService();
                    Call<RateMatchResponse> call = service.match(Integer.parseInt(rateList.get(i).getMatchId()));

                    final int finalI = i;

                    call.enqueue(new Callback<RateMatchResponse>() {
                        @Override
                        public void onResponse(Response<RateMatchResponse> response) {
                            RateMatchResponse rateMatchResponse = response.body();
                            boolean check = false;

                            int homeTeamGoal = 0;
                            int awayTeamGoal = 0;

                            if (rateMatchResponse.getFixture().getStatus().equalsIgnoreCase("FINISHED")) {
                                homeTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsHomeTeam();
                                awayTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsAwayTeam();
                                check = true;
                            }

                            if (check) {
                                if (homeTeamGoal > awayTeamGoal) {
                                    mDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                            if (ratedUser.getRatedMatches().get(finalI).getStatus()
                                                    .equalsIgnoreCase("unchecked")) {

                                                double currentPoints = Double.parseDouble(ratedUser.getCurrentPoints());
                                                double ratePoints = Double.parseDouble(rateList.get(finalI).getPoints());

                                                ratedUser.setCurrentPoints("" + (currentPoints + ratePoints));
                                                ratedUser.getRatedMatches().get(finalI).setStatus("checked");
                                                mDatabase.child(name).setValue(ratedUser);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }

                                    });
                                }

                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });

                }

                if (rateList.get(i).getTypeOfRate().equalsIgnoreCase(DRAW)   ) {
                    RateMatchService service = ApiFactory.getRateMatchService();
                    Call<RateMatchResponse> call = service.match(Integer.parseInt(rateList.get(i).getMatchId()));

                    final int finalI = i;

                    call.enqueue(new Callback<RateMatchResponse>() {
                        @Override
                        public void onResponse(Response<RateMatchResponse> response) {
                            RateMatchResponse rateMatchResponse = response.body();
                            boolean check = false;

                            int homeTeamGoal = 0;
                            int awayTeamGoal = 0;

                            if (rateMatchResponse.getFixture().getStatus().equalsIgnoreCase("FINISHED")) {
                                homeTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsHomeTeam();
                                awayTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsAwayTeam();
                                check = true;
                            }

                            if (check) {
                                if (homeTeamGoal == awayTeamGoal) {
                                    mDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                            if (ratedUser.getRatedMatches().get(finalI).getStatus()
                                                    .equalsIgnoreCase("unchecked")) {

                                                double currentPoints = Double.parseDouble(ratedUser.getCurrentPoints());
                                                double ratePoints = Double.parseDouble(rateList.get(finalI).getPoints());

                                                ratedUser.setCurrentPoints("" + (currentPoints + ratePoints));
                                                ratedUser.getRatedMatches().get(finalI).setStatus("checked");
                                                mDatabase.child(name).setValue(ratedUser);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }

                                    });
                                }

                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }

                if (rateList.get(i).getTypeOfRate().equalsIgnoreCase(WIN_SECOND)) {
                    RateMatchService service = ApiFactory.getRateMatchService();
                    Call<RateMatchResponse> call = service.match(Integer.parseInt(rateList.get(i).getMatchId()));

                    final int finalI = i;

                    call.enqueue(new Callback<RateMatchResponse>() {
                        @Override
                        public void onResponse(Response<RateMatchResponse> response) {
                            RateMatchResponse rateMatchResponse = response.body();
                            boolean check = false;

                            int homeTeamGoal = 0;
                            int awayTeamGoal = 0;

                            if (rateMatchResponse.getFixture().getStatus().equalsIgnoreCase("FINISHED")) {
                                homeTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsHomeTeam();
                                awayTeamGoal = rateMatchResponse.getFixture().getResult().getGoalsAwayTeam();
                                check = true;
                            }

                            if (check) {
                                if (homeTeamGoal < awayTeamGoal) {
                                    mDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                            if (ratedUser.getRatedMatches().get(finalI).getStatus()
                                                    .equalsIgnoreCase("unchecked")) {

                                                double currentPoints = Double.parseDouble(ratedUser.getCurrentPoints());
                                                double ratePoints = Double.parseDouble(rateList.get(finalI).getPoints());

                                                ratedUser.setCurrentPoints("" + (currentPoints + ratePoints));
                                                ratedUser.getRatedMatches().get(finalI).setStatus("checked");
                                                mDatabase.child(name).setValue(ratedUser);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }

                                    });
                                }

                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
            }
        }
    }
}
