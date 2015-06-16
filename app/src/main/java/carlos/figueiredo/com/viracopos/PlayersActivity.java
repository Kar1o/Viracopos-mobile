package carlos.figueiredo.com.viracopos;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class PlayersActivity extends ActionBarActivity {

    private EditText edtName;

    private Button btnPlayers, btnRank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);


        edtName = (EditText) findViewById(R.id.edtName);
        btnPlayers = (Button) findViewById(R.id.btnPlayers);
        btnRank = (Button) findViewById(R.id.btnRank);

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spnPlayers, android.R.layout.simple_spinner_dropdown_item);

        spnPlayers.setAdapter(adapter);*/

        btnPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtName.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "You have to insert your name first", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent startGame = new Intent(getApplicationContext(), QuizActivity.class);
                    startGame.putExtra("name", edtName.getText().toString());
                    startActivity(startGame);
                }
            }
        });

        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent checkRank = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(checkRank);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_players, menu);
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
