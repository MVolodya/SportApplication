package info.androidhive.firebase.classes.retrofit;

import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import info.androidhive.firebase.classes.retrofit.league.LeagueService;
import info.androidhive.firebase.classes.retrofit.leagueTable.LeagueTableService;
import info.androidhive.firebase.classes.retrofit.match.MatchService;
import info.androidhive.firebase.classes.retrofit.players.PlayersService;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchService;
import info.androidhive.firebase.classes.retrofit.team.TeamService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiFactory {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        CLIENT.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
    }

    @NonNull
    public static LeagueService getLeagueService() {
        return getRetrofit().create(LeagueService.class);
    }

    @NonNull
    public static MatchService getMatchService() {
        return getRetrofit().create(MatchService.class);
    }

    @NonNull
    public static LeagueTableService getTableService() {
        return getRetrofit().create(LeagueTableService.class);
    }

    @NonNull
    public static RateMatchService getRateMatchService() {
        return getRetrofit().create(RateMatchService.class);
    }

    @NonNull
    public static TeamService getTeamService() {
        return getRetrofit().create(TeamService.class);
    }

    @NonNull
    public static PlayersService getPlayerService() {
        return getRetrofit().create(PlayersService.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://api.football-data.org")
                .addConverterFactory(GsonConverterFactory.create())
                .client(CLIENT)
                .build();
    }
}
