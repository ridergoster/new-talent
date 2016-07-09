package net.andoria.newtalent.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import net.andoria.newtalent.R;
import net.andoria.newtalent.fragments.ComicFragment;
import net.andoria.newtalent.fragments.CrushFragment;
import net.andoria.newtalent.fragments.FavouriteFragment;
import net.andoria.newtalent.fragments.MusicFragment;
import net.andoria.newtalent.models.SessionData;
import net.andoria.newtalent.models.Video;
import net.andoria.newtalent.network.APIService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nvView)
    NavigationView nvView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(this.toolbar);
        setupDrawerContent(this.nvView);
        this.drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(this.drawerToggle);

        APIService.getInstance(getBaseContext()).getVideos(new APIService.APIResult<List<Video>>() {
            @Override
            public void success(List<Video> res) {
                SessionData.getInstance().setVideos(res);
                nvView.getMenu().performIdentifierAction(R.id.nav_crush_fragment, 0);
            }

            @Override
            public void error(int code, String message) {
                Log.d("FAILED : ", message + code);

            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_crush_fragment:
                fragmentClass = CrushFragment.class;
                break;
            case R.id.nav_music_fragment:
                fragmentClass = MusicFragment.class;
                break;
            case R.id.nav_comics_fragment:
                fragmentClass = ComicFragment.class;
                break;
            case R.id.nav_favourite_fragment:
                fragmentClass = FavouriteFragment.class;
                break;
            default:
                fragmentClass = CrushFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flFragment, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


}
