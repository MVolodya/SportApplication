package info.androidhive.firebase.Classes.Retrofit.Match;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FixtResponse {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("fixtures")
        @Expose
        private List<Fixtures> fixtures = new ArrayList();

        public Integer getCount() {
            return count;
        }
        public List<Fixtures> getFixtures() {
            return fixtures;
        }

    }

