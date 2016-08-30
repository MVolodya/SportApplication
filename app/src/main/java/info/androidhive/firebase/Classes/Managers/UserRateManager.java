package info.androidhive.firebase.Classes.Managers;

import java.util.List;

import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;

/**
 * Created by andrii on 30.08.16.
 */
public interface UserRateManager {
    void setUserRateList(List<RatedMatchesToDB> userRateList);
}
