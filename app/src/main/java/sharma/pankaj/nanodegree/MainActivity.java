package sharma.pankaj.nanodegree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import sharma.pankaj.nanodegree.popularmovies.PopularMoviesActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_spotify:
//                showToast("This button will launch spotify App");
                startPopularMovies();
                break;
            case R.id.btn_main_big:
                showToast("This button will launch build it bigger App");
                break;
            case R.id.btn_main_library:
                showToast("This button will launch library App");
                break;
            case R.id.btn_main_own:
                showToast("This button will launch capstone App");
                break;
            case R.id.btn_main_reader:
                showToast("This button will launch XYZ reader App");
                break;
            case R.id.btn_main_score:
                showToast("This button will launch score App");
                break;
            default:

                break;
        }
    }

    private void startPopularMovies() {
        startActivity(new Intent(this, PopularMoviesActivity.class));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
