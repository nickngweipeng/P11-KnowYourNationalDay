package nickng.p11_knowyournationalday;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    ArrayList<String> al = new ArrayList<String>();
    ArrayAdapter<String> aa;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv);

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);
        al.add("Singapore National Day is on 9 August");
        al.add("Singapore is 52 years old");
        al.add("Theme is #OneNationTogether");

        aa.notifyDataSetChanged();

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase = (LinearLayout)inflater.inflate(R.layout.accesscode, null);
        final EditText etPassphrase = (EditText)passPhrase.findViewById(R.id.editTextPassPhrase);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Login")
                .setView(passPhrase)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pp = etPassphrase.getText().toString();
                        if (pp.equalsIgnoreCase("738964")) {
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("password", pp);
                            editor.commit();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to log in successfully", Toast.LENGTH_LONG).show();
                            Log.e("Passphrase", "" + pp);
                            finish();
                        }
                    }
                })
                .setNegativeButton("No Access Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String pw = pref.getString("password", "");
        if (pw.equalsIgnoreCase("738964")) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sendFriend) {
            String[] list = new String[] { "Email", "SMS" };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    .setItems(list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
                                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                                i.putExtra(Intent.EXTRA_TEXT   , al.get(0) + "\n" + al.get(1)  + "\n" + al.get(2));
                                try {
                                    startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "SMS", Toast.LENGTH_LONG).show();
                                Intent smsIntent = new Intent(Intent.ACTION_SEND);
                                smsIntent.setType("text/plain");
                                smsIntent.putExtra(Intent.EXTRA_TEXT, al.get(0) + "\n" + al.get(1) + "\n" + al.get(2));
                                try {
                                    startActivity(Intent.createChooser(smsIntent, "Send sms..."));
                                    finish();
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(MainActivity.this, "There is no SMS client installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        } else if(item.getItemId() == R.id.quiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(passPhrase)
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            RadioGroup rg1 = (RadioGroup)passPhrase.findViewById(R. id. rg1);
                            RadioGroup rg2 = (RadioGroup)passPhrase.findViewById(R. id. rg2);
                            RadioGroup rg3 = (RadioGroup)passPhrase.findViewById(R. id. rg3);

                            int first = rg1.getCheckedRadioButtonId();
                            int second = rg2.getCheckedRadioButtonId();
                            int third = rg3.getCheckedRadioButtonId();

                            RadioButton rb1 = (RadioButton)passPhrase.findViewById(first);
                            RadioButton rb2 = (RadioButton)passPhrase.findViewById(second);
                            RadioButton rb3 = (RadioButton)passPhrase.findViewById(third);
                            int score = 0;
                            if (rb1.getText().toString().equalsIgnoreCase("No")) {
                                score += 1;
                            } else {
                            }
                            if (rb2.getText().toString().equalsIgnoreCase("Yes")) {
                                score += 1;
                            } else {
                            }
                            if (rb3.getText().toString().equalsIgnoreCase("Yes")) {
                                score += 1;
                            } else {
                            }
                            Toast.makeText(MainActivity.this, "Score " + score,
                                    Toast.LENGTH_LONG).show();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);

        } else if(item.getItemId() == R.id.quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Not really", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);

        return true;
    }
}
