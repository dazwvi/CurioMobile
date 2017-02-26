package com.example.cs349_a4;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static GetRequestTask gt1;
    public static GetRequestTask gt2;
    public static Map<String, Curio> Curio_MAP = new HashMap<String, Curio>();
    public static Map<String, User> User_MAP = new HashMap<String, User>();

    /**
     * The dummy content this fragment is presenting.
     */
    private MasterList.Project mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
        gt1 = new GetRequestTask();
        gt2 = new GetRequestTask();
    }

    public void get_curio_list() {
        try {
            String output = gt1.execute("http://test.crowdcurio.com/api/curio/").get();
            JSONArray jsonArray = new JSONArray(output);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String question = jsonObject.getString("question");
                Curio item = new Curio(id, question);
                Curio_MAP.put(item.id, item);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void get_team_list(){
        try {
            String output = gt2.execute("http://test.crowdcurio.com/api/user/profile/").get();
            JSONObject jsonObject = new JSONObject(output);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject user = jsonArray.getJSONObject(i);
                String id = user.getString("id");
                String nickname = user.getString("nickname");
                String avatar = user.getString("avatar");
                String bio = user.getString("bio");
                String title = user.getString("title");

                User item = new User(id, nickname, avatar, bio, title);
                User_MAP.put(item.id, item);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mItem = MasterList.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.name);
            ImageView iv =  (ImageView) appBarLayout.findViewById(R.id.imageView1);
            String image_URL = mItem.avatar;
            new ImageLoadTask(image_URL, iv).execute();
            //iv.getLayoutParams().height = 800;
            //iv.getLayoutParams().width = 800;
        }

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail_desc)).setText(mItem.description);

            get_curio_list();
            JSONArray curios = mItem.curios;
            for (int i = 0; i < curios.length(); i++){
                try {
                    String key = curios.get(i).toString();
                    String question = Curio_MAP.get(key).question;
                    TextView msg = new TextView(getActivity());
                    msg.setTextSize(20);
                    msg.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
                    msg.setText(question);
                    LinearLayout chat = (LinearLayout) rootView.findViewById(R.id.item_detail_contribute);
                    chat.addView(msg);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            get_team_list();
            JSONArray teams = mItem.team;
            for (int i = 0; i < teams.length(); i++){
                try {
                    JSONObject member = teams.getJSONObject(i);
                    String key = member.getString("id");
                    User team = User_MAP.get(key);
                    ImageView iv = new ImageView(getActivity());

                    new ImageLoadTask(team.avatar, iv).execute();

                    TextView msg1 = new TextView(getActivity());
                    msg1.setText(team.nickname);
                    TextView msg2 = new TextView(getActivity());
                    msg2.setText(team.bio);
                    TextView msg3 = new TextView(getActivity());
                    msg3.setText(team.title);
                    LinearLayout chat = (LinearLayout) rootView.findViewById(R.id.item_detail_team);

                    msg1.setGravity(Gravity.CENTER);
                    msg2.setGravity(Gravity.CENTER);
                    msg3.setGravity(Gravity.CENTER);
                    msg1.setTextSize(20);
                    msg2.setTextSize(20);
                    msg3.setTextSize(20);

                    chat.addView(iv);
                    iv.getLayoutParams().height = 200;
                    iv.getLayoutParams().width = 200;
                    chat.addView(msg1);
                    chat.addView(msg2);
                    chat.addView(msg3);

                    chat.setGravity(Gravity.CENTER);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

        return rootView;
    }

    /**
     * A Curio item representing a piece of content.
     */
    public static class Curio {
        public final String id;
        public final String question;

        public Curio (String id, String question) {
            this.id = id;
            this.question = question;
        }
    }
    /**
     * A Curio item representing a piece of content.
     */
    public static class User {
        public final String id;
        public final String nickname;
        public final String avatar;
        public final String bio;
        public final String title;

        public User (String id, String nickname, String avatar, String bio, String title) {
            this.id = id;
            this.nickname = nickname;
            this.avatar = avatar;
            this.bio = bio;
            this.title = title;
        }
    }
}
