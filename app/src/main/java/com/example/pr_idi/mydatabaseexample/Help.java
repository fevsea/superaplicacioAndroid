package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Help extends BackToolBar {

    private ExpandableListView expandable;
    private List<Map<String, String>> groupListItem;
    private List<List<Map<String, String>>> childListItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        super.onCreate("Help");

        groupListItem = new ArrayList<Map<String, String>>();
        childListItem = new ArrayList<List<Map<String, String>>>();


        addNewElement("Add new book", "press the red button and fill the params next press create botton");
        addNewElement("Delete a book", "swipe to the left");
        addNewElement("Find a book", "press the lent icon and write the name/autor/category");
        addNewElement("Eval a book", "keep press a book and select how many starts deservs");
        addNewElement("See info of a book", "press softly a element of the list");

    }

    private void addNewElement(String name, String value) {
        Map<String, String> group1 = new HashMap<String, String>();
        groupListItem.add(group1);
        group1.put("parentItem", name);

        List<Map<String, String>> children1 = new ArrayList<Map<String, String>>();
        Map<String, String> childrenitem1 = new HashMap<String, String>();
        children1.add(childrenitem1);
        childrenitem1.put("childItem", value);
        childListItem.add(children1);

        ExpandableListAdapter mAdapterView = new SimpleExpandableListAdapter(
                this,
                groupListItem,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"parentItem"},
                new int[]{android.R.id.text1, android.R.id.text2},
                childListItem,
                 android.R.layout.simple_expandable_list_item_2,
                new String[]{"childItem"},
                new int[]{android.R.id.text1}
        );
        expandable = (ExpandableListView) findViewById(R.id.expandableList);
        expandable.setAdapter(mAdapterView);
    }
}
