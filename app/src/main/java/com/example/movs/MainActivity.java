package com.example.movs;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, selectedItemInformationDetailsInterface, YoutubeInterface, DetailsInterface{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private TextView title;
    private CoordinatorLayout coordinatorLayout;

    private MoviesFragment moviesFragment;
    private AccountFragment accountFragment;
    private UpcomingFragment upcomingFragment;
    private TrailersFragment trailersFragment;
    private PopularFragment popularFragment;
    private TopRatedFragment topRatedFragment;
    private NowShowingFragment nowShowingFragment;
    private DetailsFragment detailsFragment;
    private VideoDisplayFragment videoDisplayFragment;
    private favouritesFragment favorite;
    private SelectedGenreFragment selectedGenreFragment;
    private SearchFragment searchFragment;

    private ArrayList<fragmentClass> fragmentClasses = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private int EditCount = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.back));
        drawerClass();
        bottomClass();
        init();

    }

    private void init() {
        title.setText(R.string.nav_mov);
        if (moviesFragment == null) {
            moviesFragment = new MoviesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame, moviesFragment, "Movies").commit();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            tags.add("Movies");
            fragmentClasses.add(new fragmentClass("Movies", moviesFragment));
        }
        else {
            tags.remove("Movies");
            tags.add("Movies");
        }
        setVisibility("Movies");
    }

    private void bottomClass() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.bottomMovies:
                        title.setText(R.string.nav_mov);
                        if (moviesFragment == null) {
                            moviesFragment = new MoviesFragment();
                            fragmentTransaction.add(R.id.frame, moviesFragment, "Movies");
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            fragmentTransaction.commit();
                            tags.add("Movies");
                            fragmentClasses.add(new fragmentClass("Movies", moviesFragment));
                        }
                        else{
                            tags.remove("Movies");
                            tags.add("Movies");
                        }
                        setVisibility("Movies");
                        break;
                    case R.id.bottomUpcoming:
                        title.setText(R.string.nav_up);
                        if (upcomingFragment == null) {
                            upcomingFragment = new UpcomingFragment();
                            fragmentTransaction.add(R.id.frame, upcomingFragment, "Upcoming");
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            fragmentTransaction.commit();
                            tags.add("Upcoming");
                            fragmentClasses.add(new fragmentClass("Upcoming", upcomingFragment));
                        }
                        else {
                            tags.remove("Upcoming");
                            tags.add("Upcoming");
                        }
                        setVisibility("Upcoming");
                        break;
                    case R.id.bottomTrailers:
                        title.setText(R.string.now_showing_title);
                        if (nowShowingFragment == null) {
                            nowShowingFragment = new NowShowingFragment();
                            fragmentTransaction.add(R.id.frame, nowShowingFragment, "NowShowing");
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            fragmentTransaction.commit();
                            tags.add("NowShowing");
                            fragmentClasses.add(new fragmentClass("NowShowing", nowShowingFragment));
                        }
                        else {
                            tags.remove("NowShowing");
                            tags.add("NowShowing");
                        }
                        setVisibility("NowShowing");
                        break;
                    case R.id.bottomMe:
                        title.setText(R.string.top_rated_title);
                        if (topRatedFragment == null) {
                            topRatedFragment = new TopRatedFragment();
                            fragmentTransaction.add(R.id.frame, topRatedFragment, "TopRated");
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            fragmentTransaction.commit();
                            tags.add("TopRated");
                            fragmentClasses.add(new fragmentClass("TopRated", topRatedFragment));
                        }
                        else {
                            tags.remove("TopRated");
                            tags.add("TopRated");
                        }
                        setVisibility("TopRated");
                        break;
                }
                return true;
            }
        });

    }

    private void drawerClass() {
        toolbar = (Toolbar) findViewById(R.id.toolbarDrawer);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        title = (TextView) findViewById(R.id.toolTitle);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.corTool);
        coordinatorLayout.setVisibility(View.VISIBLE);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close
        );
        toolbar.setNavigationIcon(R.drawable.ic_sort_black_24dp);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void bottomChecked(String tagName){
        Menu menu = (Menu) bottomNavigationView.getMenu();
        MenuItem menuItem = null;
        if (tagName.equals("Movies")){
            menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        }
        else if (tagName.equals("Upcoming")){
            menuItem = menu.getItem(1);
            menuItem.setChecked(true);
        }
        else if (tagName.equals("NowShowing")){
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
        }
        else if (tagName.equals("TopRated")){
            menuItem = menu.getItem(3);
            menuItem.setChecked(true);
        }

    }

    private void navigationChecked(String tagName){
        Menu menu = (Menu) navigationView.getMenu();
        MenuItem menuItem = null;
        if (tagName.equals("Movies")){
            menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        }
        else if (tagName.equals("Upcoming")){
            menuItem = menu.getItem(1);
            menuItem.setChecked(true);
        }
        else if (tagName.equals("NowShowing")){
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
        }
    }

    private void setVisibility(String tagName){
        if (tagName.equals("Movies")){
            showBottomNavigation();
            coordinatorLayout.setVisibility(View.VISIBLE);
            title.setText(R.string.nav_mov);
        }
        else if (tagName.equals("DetailsNow")){
            hideBottomNavigation();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tagName.equals("NowShowing")){
            showBottomNavigation();
            title.setText(R.string.now_showing_title);
            coordinatorLayout.setVisibility(View.VISIBLE);
        }
        else if (tagName.equals("Upcoming")){
            showBottomNavigation();
            coordinatorLayout.setVisibility(View.VISIBLE);
            title.setText(R.string.nav_up);
        }
        else if (tagName.equals("Account")){
            showBottomNavigation();
            coordinatorLayout.setVisibility(View.VISIBLE);
        }
        else if (tagName.equals("Trailers")) {
            hideBottomNavigation();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tagName.equals("Video")) {
            hideBottomNavigation();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tagName.equals("TopRated")){
            showBottomNavigation();
            coordinatorLayout.setVisibility(View.VISIBLE);
            title.setText(R.string.top_rated_title);
        }
        else if (tagName.equals("Popular")){
            showBottomNavigation();
            title.setText(R.string.popular_title_sample);
            coordinatorLayout.setVisibility(View.VISIBLE);
        }
        else if (tagName.equals("SelectedGenre")){
            showBottomNavigation();
            coordinatorLayout.setVisibility(View.VISIBLE);
        }
        else if (tagName.equals("Search")){
            hideBottomNavigation();
            coordinatorLayout.setVisibility(View.GONE);
        }


        for (int i = 0; i < fragmentClasses.size(); i++){
            if (tagName.equals(fragmentClasses.get(i).getTag())){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(fragmentClasses.get(i).getFragment());
                fragmentTransaction.commit();
            }
            else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(fragmentClasses.get(i).getFragment());
                fragmentTransaction.commit();
            }
            bottomChecked(tagName);
            navigationChecked(tagName);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
    }

    @Override
    public void onBackPressed() {
        int totalTags = tags.size();
        if (totalTags > 1){
            String top = tags.get(totalTags - 1);
            String bottom = tags.get(totalTags -2);
            setVisibility(bottom);
            tags.remove(top);
            EditCount = 0;
        }
        else if (totalTags == 1){
            String topString = tags.get(totalTags - 1);
            if (topString.equals("Movies")) {
                EditCount++;
                Toast.makeText(this, "End", Toast.LENGTH_SHORT).show();
            }
            else{
                EditCount ++;
            }
        }
        if (EditCount >= 2){
            super.onBackPressed();
        }

    }

    private void showBottomNavigation(){
        if (bottomNavigationView != null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

    }

    private void hideBottomNavigation(){
        if (bottomNavigationView != null){
            bottomNavigationView.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.Home:
                title.setText(R.string.nav_mov);
                if (moviesFragment == null) {
                    moviesFragment = new MoviesFragment();
                    fragmentTransaction.add(R.id.frame, moviesFragment, "Movies");
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.commit();
                    tags.add("Movies");
                    fragmentClasses.add(new fragmentClass("Movies", moviesFragment));
                }
                else {
                    tags.remove("Movies");
                    tags.add("Movies");
                }
                setVisibility("Movies");
                break;
            case R.id.UpcomingMovies:
                title.setText(R.string.nav_up);
                if (upcomingFragment == null) {
                    upcomingFragment = new UpcomingFragment();
                    fragmentTransaction.add(R.id.frame, upcomingFragment, "Upcoming");
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.commit();
                    tags.add("Upcoming");
                    fragmentClasses.add(new fragmentClass("Upcoming", upcomingFragment));
                }
                else {
                    tags.remove("Upcoming");
                    tags.add("Upcoming");
                }
                setVisibility("Upcoming");
                break;
            case R.id.NowShowing:
                title.setText(R.string.now_showing_title);
                if (nowShowingFragment == null) {
                    nowShowingFragment = new NowShowingFragment();
                    fragmentTransaction.add(R.id.frame, nowShowingFragment, "NowShowing");
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.commit();
                    tags.add("NowShowing");
                    fragmentClasses.add(new fragmentClass("NowShowing", nowShowingFragment));
                }
                else {
                    tags.remove("NowShowing");
                    tags.add("NowShowing");
                }
                setVisibility("NowShowing");
                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, Settings.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Download app");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Choose App"));
                break;

        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void getNowShowingSelectedItemInformation(NowPlaying nowPlaying) {
        if (detailsFragment != null){
            getSupportFragmentManager().beginTransaction().remove(detailsFragment).commitAllowingStateLoss();
        }
        detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("Now", nowPlaying);
        detailsFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, detailsFragment, "DetailsNow");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        tags.add("DetailsNow");
        fragmentClasses.add(new fragmentClass("DetailsNow", detailsFragment));
        setVisibility("DetailsNow");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    @Override
    public void getUpcomingSelectedItem(Upcoming upcoming) {
        if (detailsFragment != null){
            getSupportFragmentManager().beginTransaction().remove(detailsFragment).commitAllowingStateLoss();
        }
        detailsFragment = new DetailsFragment();
        Bundle argsUpcoming = new Bundle();
        argsUpcoming.putParcelable("Upcoming", upcoming);
        detailsFragment.setArguments(argsUpcoming);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, detailsFragment, "DetailsNow");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        tags.add("DetailsNow");
        fragmentClasses.add(new fragmentClass("DetailsNow", detailsFragment));
        setVisibility("DetailsNow");

    }

    @Override
    public void getPopularSelectedItem(Popular popular) {
        if (detailsFragment != null){
            getSupportFragmentManager().beginTransaction().remove(detailsFragment).commitAllowingStateLoss();
        }
        detailsFragment = new DetailsFragment();
        Bundle argsPopular = new Bundle();
        argsPopular.putParcelable("Popular", popular);
        detailsFragment.setArguments(argsPopular);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, detailsFragment, "DetailsNow");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        tags.add("DetailsNow");
        fragmentClasses.add(new fragmentClass("DetailsNow", detailsFragment));
        setVisibility("DetailsNow");

    }

    @Override
    public void getMore() {
        title.setText(R.string.now_showing_title);
        if (nowShowingFragment == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            nowShowingFragment = new NowShowingFragment();
            fragmentTransaction.add(R.id.frame, nowShowingFragment, "NowShowing");
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.commit();
            tags.add("NowShowing");
            fragmentClasses.add(new fragmentClass("NowShowing", nowShowingFragment));
        }
        else {
            tags.remove("NowShowing");
            tags.add("NowShowing");
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setVisibility("NowShowing");
    }

    @Override
    public void getUpcomingMore() {
        title.setText(R.string.nav_up);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (upcomingFragment == null) {
            upcomingFragment = new UpcomingFragment();
            fragmentTransaction.add(R.id.frame, upcomingFragment, "Upcoming");
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.commit();
            tags.add("Upcoming");
            fragmentClasses.add(new fragmentClass("Upcoming", upcomingFragment));
        }
        else {
            tags.remove("Upcoming");
            tags.add("Upcoming");
        }
        setVisibility("Upcoming");

    }

    @Override
    public void getPopularMore() {
        title.setText(R.string.popular_title_sample);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (popularFragment == null){
            popularFragment = new PopularFragment();
            fragmentTransaction.add(R.id.frame, popularFragment, "Popular");
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.commit();
            tags.add("Popular");
            fragmentClasses.add(new fragmentClass("Popular", popularFragment));
        }
        else {
            tags.remove("Popular");
            tags.add("Popular");
        }
        setVisibility("Popular");

    }

    @Override
    public void getTopRatedMore() {
        title.setText(R.string.top_rated_title_sample);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (topRatedFragment == null){
            topRatedFragment = new TopRatedFragment();
            fragmentTransaction.add(R.id.frame, topRatedFragment, "TopRated");
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.commit();
            tags.add("TopRated");
            fragmentClasses.add(new fragmentClass("TopRated", topRatedFragment));
        }
        else {
            tags.remove("TopRated");
            tags.add("TopRated");
        }
        setVisibility("TopRated");

    }

    @Override
    public void getTopRatedItem(TopRated topRated) {
        if (detailsFragment != null){
            getSupportFragmentManager().beginTransaction().remove(detailsFragment).commitAllowingStateLoss();
        }
        detailsFragment = new DetailsFragment();
        Bundle argsTop = new Bundle();
        argsTop.putParcelable("TopRated", topRated);
        detailsFragment.setArguments(argsTop);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, detailsFragment, "DetailsNow");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        tags.add("DetailsNow");
        fragmentClasses.add(new fragmentClass("DetailsNow", detailsFragment));
        setVisibility("DetailsNow");
    }



    @Override
    public void getYouTubeVideo(TrailersInfo trailersInfo) {
        if (videoDisplayFragment != null){
            getSupportFragmentManager().beginTransaction().remove(videoDisplayFragment).commitAllowingStateLoss();
        }
        videoDisplayFragment = new VideoDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable("MVideo", trailersInfo);
        videoDisplayFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, videoDisplayFragment, "Video");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        tags.add("Video");
        fragmentClasses.add(new fragmentClass("Video", videoDisplayFragment));
        setVisibility("Video");
    }

    @Override
    public void getDetailsId(int myID) {
        if (trailersFragment != null){
            getSupportFragmentManager().beginTransaction().remove(trailersFragment).commitAllowingStateLoss();
        }
        trailersFragment = new TrailersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("MyId", myID);
        trailersFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, trailersFragment, "Trailers");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
        tags.add("Trailers");
        fragmentClasses.add(new fragmentClass("Trailers", trailersFragment));
        setVisibility("Trailers");
    }


    @Override
    public void getGenre(Genres genres) {
        if (selectedGenreFragment != null){
            getSupportFragmentManager().beginTransaction().remove(selectedGenreFragment).commitAllowingStateLoss();
        }
        String gTitle = genres.getName();
        title.setText(gTitle);
        selectedGenreFragment = new SelectedGenreFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Genre", genres);
        selectedGenreFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame, selectedGenreFragment, "SelectedGenre");
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        tags.add("SelectedGenre");
        fragmentClasses.add(new fragmentClass("SelectedGenre", selectedGenreFragment));
        transaction.commit();
        setVisibility("SelectedGenre");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_item, menu);
        /**MenuItem menuItem2 = menu.findItem(R.id.icon_search);
        materialSearchView.setMenuItem(menuItem2);**/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, Settings.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.icon_search:
                if (searchFragment == null){
                    searchFragment = new SearchFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.frame, searchFragment, "Search");
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.commit();
                    tags.add("Search");
                    fragmentClasses.add(new fragmentClass("Search", searchFragment));
                }
                else {
                    tags.remove("Search");
                    tags.add("Search");
                }
                setVisibility("Search");
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
