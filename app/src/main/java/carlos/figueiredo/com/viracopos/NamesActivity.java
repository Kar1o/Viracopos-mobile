package carlos.figueiredo.com.viracopos;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class NamesActivity extends ActionBarActivity {

    private EditText edtPlayer1, edtPlayer2, edtPlayer3, edtPlayer4 ;

    private TextView txtPlayer2, txtPlayer3, txtPlayer4;

    private Button btnNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        final int qtdPlayers = getIntent().getIntExtra("qtdPlayers", 2);

        edtPlayer1 = (EditText) findViewById(R.id.editText);
        edtPlayer2 = (EditText) findViewById(R.id.editText2);
        edtPlayer3 = (EditText) findViewById(R.id.editText3);
        edtPlayer4 = (EditText) findViewById(R.id.editText4);

        txtPlayer2 = (TextView) findViewById(R.id.textView2);
        txtPlayer3 = (TextView) findViewById(R.id.textView3);
        txtPlayer4 = (TextView) findViewById(R.id.textView4);

        btnNames = (Button) findViewById(R.id.btnNames);


        if (qtdPlayers <= 3) {
            txtPlayer4.setVisibility(View.INVISIBLE);
            edtPlayer4.setVisibility(View.INVISIBLE);
             if (qtdPlayers <= 2) {
                txtPlayer3.setVisibility(View.INVISIBLE);
                edtPlayer3.setVisibility(View.INVISIBLE);
                 if (qtdPlayers == 1){
                     txtPlayer2.setVisibility(View.INVISIBLE);
                     edtPlayer2.setVisibility(View.INVISIBLE);
                 }
            }
        }

        btnNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qtdPlayers >= 1 && edtPlayer1.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Insert name to all players", Toast.LENGTH_SHORT).show();
                    if (qtdPlayers >= 2 && edtPlayer2.getText().toString().equals("")) {
                        Toast.makeText(getBaseContext(), "Insert name to all players", Toast.LENGTH_SHORT).show();
                        if (qtdPlayers >= 3 && edtPlayer3.getText().toString().equals("")) {
                            Toast.makeText(getBaseContext(), "Insert name to all players", Toast.LENGTH_SHORT).show();
                            if (qtdPlayers >= 4 && edtPlayer4.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), "Insert name to all players", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                else {
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("qtdPlayers", qtdPlayers);
                    intent.putExtra("Player1", edtPlayer1.getText());

                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_names, menu);
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
