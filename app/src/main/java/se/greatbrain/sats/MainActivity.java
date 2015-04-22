package se.greatbrain.sats;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import se.greatbrain.sats.handler.CentersResponseHandler;
import se.greatbrain.sats.handler.ClassCategoriesResponseHandler;
import se.greatbrain.sats.handler.InstructorsResponseHandler;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.deleteRealmFile(this);

//        ClassTypesResponseHandler classTypesResponseHandler = new ClassTypesResponseHandler(this);
//        classTypesResponseHandler.get();

        CentersResponseHandler centersResponseHandler =  new CentersResponseHandler(this);
        centersResponseHandler.get();

        InstructorsResponseHandler instructorsResponseHandler = new InstructorsResponseHandler(this);
        instructorsResponseHandler.get();

        ClassCategoriesResponseHandler classCategoriesResponseHandler = new ClassCategoriesResponseHandler(this);
        classCategoriesResponseHandler.get();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
