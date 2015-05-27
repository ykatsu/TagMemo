package com.e_dazi.tagmemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
    }

    public void onSectionAttached(int number) {
        ArrayList<Tag> tagl = ((MainApplication) getApplication()).getDrawerTagList();
        mTitle = tagl.get(number).name;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final int REQUEST_REGMEMO_ACTIVITY = 1;

        private ArrayAdapter<String> mAdapter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView)rootView.findViewById(R.id.section_label);

            Bundle args = getArguments();
            int tagPos = args.getInt(ARG_SECTION_NUMBER);
            ArrayList<Tag> tagl = ((MainApplication) this.getActivity().getApplication()).getDrawerTagList();
            Tag tag = tagl.get(tagPos);
            String tagStr = tag.name;
            long tagId = tag.getId();

            //textView.setText("テキスト：" + tagStr);
            //Toast.makeText(this, text, Toast.LENGTH_LONG).show();

            ArrayList<Item> listItem = new ArrayList<>(
                new Select()
                    .from(Item.class)
                    .where("Tag = ?", tagId)
                    //.orderBy("updated_at DESC")
                    .<Item>execute()
            );

            // sort by memo.update_at DESC
            Collections.sort(listItem, new ItemComparator());

            ArrayList<String> list = new ArrayList<>();
            for (Item item: listItem) {
                list.add(item.memo.title);
            }

            ListView listView = (ListView) rootView.findViewById(R.id.item_listview);
            mAdapter = new ItemListAdapter(getActivity(), list);
            listView.setAdapter(mAdapter);

            // リストをタップした時の動作を定義する
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull AdapterView<?> parent, @NonNull View view, int position, long id) {
                    // Adapterからタップした位置のデータを取得する
                    String str = (String) parent.getItemAtPosition(position);
                    //Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
                    gotoMemoActivity(str);
                }
            });

            // 追加ボタンの動作を定義する
            Button headerButton = (Button)rootView.findViewById(R.id.item_add_button);
            headerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View v) {
                    registerMemo();
                }
            });
            return rootView;
        }

        private void gotoMemoActivity(String str) {
            Intent intent = new Intent( getActivity(), MemoActivity.class );
            intent.putExtra( "INTENT_PARAM", str );
            startActivity( intent );
        }

        private void registerMemo() {
            // TODO: RegisterMemoActivity 作成
//            Intent intent = new Intent( this, RegisterMemoActivity.class);
//            startActivityForResult(intent, REQUEST_REGMEMO_ACTIVITY);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
