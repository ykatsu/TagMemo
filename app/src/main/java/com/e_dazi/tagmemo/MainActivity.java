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

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

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

    /**
     * ドロワーから選択された
     *
     * @param number selected position
     */
    public void onSectionAttached(int number) {
        // 選択位置のタグオブジェクトを取得し、タイトルに設定
        Tag tag = getNavigationDrawerFragment().getAdapter().getItem(number);
        mTitle = tag.name;
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
        private static final int REQUEST_ADDMEMO_ACTIVITY = 1;
        private static final int REQUEST_EDITMEMO_ACTIVITY = 2;

        private ArrayAdapter<Item> mAdapter;
        private Tag mTag;
//        private ArrayList<Item> mListItem;

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
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // グローバルに置いてあるタグリストから、パラ指定されたオブジェクトを取り出す。
            Bundle args = getArguments();
            int tagPos = args.getInt(ARG_SECTION_NUMBER);
            mTag = ((MainActivity) getActivity()).getNavigationDrawerFragment().getAdapter().getItem(tagPos);
            long tagId = mTag.getId();

//            String tagStr = mTag.name;
//            TextView textView = (TextView)rootView.findViewById(R.id.section_label);
//            textView.setText("テキスト：" + tagStr);
//            Toast.makeText(this, text, Toast.LENGTH_LONG).show();

            // ListViewを作る
            ListView listView = getListView(rootView, tagId);

            // リストをタップした時の動作を定義する
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull AdapterView<?> parent, @NonNull View view, int position, long id) {
                    // Adapterからタップした位置のデータを取得する
                    //String title = (String) parent.getItemAtPosition(position);
                    //Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();

                    editMemoActivity(mAdapter.getItem(position).memo.getId());
                }
            });

            // 追加ボタンの動作を定義する
            Button headerButton = (Button) rootView.findViewById(R.id.item_add_button);
            headerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View v) {
                    addMemoActivity(mTag.getId());
                }
            });

            return rootView;
        }

        /**
         * Prepare ListView from tag ID.
         * @param rootView root view
         * @param tagId tagId of data to load.
         * @return ListView Object.
         */
        private ListView getListView(View rootView, long tagId) {
            mAdapter = new ItemListAdapter(getActivity(), new ArrayList<Item>(), tagId);
            ListView listView = (ListView) rootView.findViewById(R.id.item_listview);
            listView.setAdapter(mAdapter);
            return listView;
        }

        /**
         * メモ編集画面へ移動
         * @param memoId memoId to edit.
         */
        private void editMemoActivity(long memoId) {
            Intent intent = new Intent(getActivity(), MemoActivity.class);
            intent.putExtra(getString(R.string.param_memo_id), memoId);
//            startActivity(intent);
            startActivityForResult(intent, REQUEST_EDITMEMO_ACTIVITY);
        }

        /** 新規メモ追加画面へ移動
         *
         * @param tagId tagId of default tag of new memo.
         */
        private void addMemoActivity(long tagId) {
            Intent intent = new Intent(getActivity(), MemoActivity.class);
            intent.putExtra(getString(R.string.param_memo_id),
                    (long) getResources().getInteger(R.integer.memo_id_none));
            intent.putExtra(getString(R.string.param_tag_id), tagId);
            startActivityForResult(intent, REQUEST_ADDMEMO_ACTIVITY);
        }

        /**
         * メモ画面からの戻った時の処理
         *
         * @param requestCode requestCode
         * @param resultCode resultCode
         * @param data data
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                // Memo add からの戻り値取得
                case REQUEST_ADDMEMO_ACTIVITY:
                    // Memo Edit からの戻り値取得
                case REQUEST_EDITMEMO_ACTIVITY:
                    if (Activity.RESULT_OK == resultCode) {
                        // 保存処理が実行された時の処理
//                        String title = data.getStringExtra(getString(R.string.key_title));
                        // メモリストをリロード
                        ((ItemListAdapter)mAdapter).refresh(mTag.getId());

                        // タグのリストをリロード
                        if (data.getBooleanExtra(getString(R.string.param_doRefreshDrawer), true)) {
                            ((MainActivity)getActivity()).getNavigationDrawerFragment().getAdapter().refresh();
                        }
                    }
                    break;
            }
        }
    }
}
