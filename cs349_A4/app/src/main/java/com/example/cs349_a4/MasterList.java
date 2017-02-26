package com.example.cs349_a4;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.net.HttpURLConnection;
import java.io.*;
import android.os.*;
import org.json.*;

import java.util.StringTokenizer;

public class MasterList {

    private static MasterList _instance;

    public static GetRequestTask gt;
    public static List<Project> ITEMS = new ArrayList<Project>();
    /**
     * A map of Project items, by ID.
     */
    public static Map<String, Project> ITEM_MAP = new HashMap<String, Project>();

    public MasterList(){
        gt = new GetRequestTask();
        //gt.delegate = this;
        get_master_list();
    }

    public synchronized static MasterList getInstance()
    {
        if (_instance == null)
        {
            _instance = new MasterList();
        }
        return _instance;
    }

    public void get_master_list(){
        try {
            String output = gt.execute("http://test.crowdcurio.com/api/project/").get();
            JSONArray jsonArray = new JSONArray(output);
            for (int i  = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String avatar = jsonObject.getString("avatar");
                String description = jsonObject.getString("description");
                JSONArray curios = jsonObject.getJSONArray("curios");
                JSONArray team = jsonObject.getJSONArray("team");
                Project item = new Project(id, name, avatar, description, curios, team, i+1);
                addItem(item);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update_master_list(){
        this.ITEM_MAP.clear();
        this.ITEMS.clear();

        GetRequestTask gt_temp = new GetRequestTask();
        try {
            String output = gt_temp.execute("http://test.crowdcurio.com/api/project/").get();
            JSONArray jsonArray = new JSONArray(output);
            for (int i  = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String avatar = jsonObject.getString("avatar");
                String description = jsonObject.getString("description");
                JSONArray curios = jsonObject.getJSONArray("curios");
                JSONArray team = jsonObject.getJSONArray("team");
                Project item = new Project(id, name, avatar, description, curios, team, i+1);
                addItem(item);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void advance_search(String s, int position){
        this.ITEM_MAP.clear();
        this.ITEMS.clear();
        GetRequestTask gt_temp = new GetRequestTask();

        String URL = "http://test.crowdcurio.com/api/project/?";
        switch (position){
            case 0:
                URL += "description=";
                break;
            case 1:
                URL += "data_type=";
                break;
            case 2:
                URL += "redirect_url=";
                break;
            case 3:
                URL += "name=";
                break;
            case 4:
                URL += "short_description=";
                break;
        }

        try {
            String output = gt_temp.execute(URL + s).get();
            JSONArray jsonArray = new JSONArray(output);
            for (int i  = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String avatar = jsonObject.getString("avatar");
                String description = jsonObject.getString("description");
                JSONArray curios = jsonObject.getJSONArray("curios");
                JSONArray team = jsonObject.getJSONArray("team");
                Project item = new Project(id, name, avatar, description, curios, team, i+1);
                addItem(item);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addItem(Project item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A Project item representing a piece of content.
     */
    public static class Project {
        public final int num_on_the_list;
        public final String id;
        public final String name;
        public final String avatar;
        public final String description;
        public final JSONArray curios;
        public final JSONArray team;

        public Project (String id, String name, String avatar, String description, JSONArray curios, JSONArray team, Integer num) {
            this.id = id;
            this.name = name;
            this.avatar = avatar;
            this.description = description;
            this.curios = curios;
            this.team = team;
            this.num_on_the_list = num;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
