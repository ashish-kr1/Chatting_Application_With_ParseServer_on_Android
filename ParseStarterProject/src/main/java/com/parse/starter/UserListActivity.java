package com.parse.starter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;
    EditText editText;

    public void back(View view){
        Intent intent = new Intent(getApplicationContext(), Browser.class);
        startActivity(intent);
    }
    public void note(View view){
        Intent intent = new Intent(getApplicationContext(), notepad.class);
        startActivity(intent);
    }
public void game(View view){
    Intent intent = new Intent(getApplicationContext(), gameActivity.class);
    startActivity(intent);
}

public void galary(View view){
    Intent intent = new Intent(getApplicationContext(), listActivity.class);
    startActivity(intent);
}


    public void getPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }
    public void searchItem(String textToSearch)
    {
        arrayAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getPhoto();

            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.share) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    getPhoto();

            }

        } else if (item.getItemId() == R.id.logout) {

            new AlertDialog.Builder(UserListActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Leaving the session !")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    ParseUser.logOut();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                }
                            }
                    )
                    .setNegativeButton("No", null)
                    .show();

            return true;



        }else if (item.getItemId()==R.id.message){
            Intent intent = new Intent(getApplicationContext(), offline.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                Log.i("Photo", "Received");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("image.png", byteArray);

                ParseObject object = new ParseObject("Image");

                object.put("image", file);

                object.put("username", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            Toast.makeText(UserListActivity.this, "Image Shared!", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(UserListActivity.this, "Image could not be shared - please try again later.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);




/////


        setTitle("MooDf User's");

        ListView userListView = (ListView) findViewById(R.id.userListView);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

                intent.putExtra("username", users.get(i));

                startActivity(intent);




            }
        });

        users.clear();





        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);

        userListView.setAdapter(arrayAdapter);

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseUser user : objects) {

                            users.add(user.getUsername());




                        }

                        arrayAdapter.notifyDataSetChanged();

                    }

                }

            }
        });


    }
}
