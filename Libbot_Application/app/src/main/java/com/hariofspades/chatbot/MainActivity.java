package com.hariofspades.chatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;
import com.hariofspades.chatbot.Adapter.ChatMessageAdapter;
import com.hariofspades.chatbot.Pojo.BookBean;
import com.hariofspades.chatbot.Pojo.ChatMessage;
import com.hariofspades.chatbot.Pojo.ResponseBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import retrofit.APIClient;
import retrofit.RetrofitClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    public boolean result;
    public Bot bot;
    public static Chat chat;
    private ChatMessageAdapter mAdapter;
    private RetrofitClientInterface retrofitClientInterface;
    private String res="";
    private Button Search_button;
    private EditText Book_Name;
    private EditText Author_Name;
    public String ans="";
    AlertDialog dialog ;
    AlertDialog.Builder builder;
    public SharedPreferences sharedpreferences;
    private EditText Book_Id;
    private Button Issue_btn;
    private EditText RBook_Name, RAuthor_Name, RPublisher_Name;
    private Button RRequest_Btn;
    private EditText Fine_Amt;
    private Button btn_payonline, btn_payoffline;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Tools.checkPermission(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = (FloatingActionButton) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);
        retrofitClientInterface =  APIClient.getClient().create(RetrofitClientInterface.class);


        mButtonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                //bot
                /*if(message.equalsIgnoreCase("Take me to library") || message.equalsIgnoreCase("Library"))
                {
                    sendMessage(message);
                    String ans= "Are you a registered user?";
                    mimicOtherMessage(ans);
                    mEditTextMessage.setText("");
                }*/
                if (message.equalsIgnoreCase("Yes I am a registered user")) {
                    sendMessage(message);
                    String ans= "Just a minute. Taking you to Login screen";
                    mimicOtherMessage(ans);
                    mEditTextMessage.setText("");
                    mListView.setSelection(mAdapter.getCount() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 5000);

                }
                else if (message.equalsIgnoreCase("No I am not")) {
                    sendMessage(message);
                    mimicOtherMessage("No problem! Taking you to Signup screen");
                    mEditTextMessage.setText("");
                    mListView.setSelection(mAdapter.getCount() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, SignupActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 10000);
                }
                else if (message.equalsIgnoreCase("Search books in library")){

                    Toast.makeText(MainActivity.this, "Just a minute", Toast.LENGTH_SHORT).show();

                    sendMessage(message);
                    mEditTextMessage.setText("");
                    getBooks();
                    mListView.setSelection(mAdapter.getCount() - 1);

                }
                else if (message.equalsIgnoreCase("search book" )){

                    Toast.makeText(MainActivity.this, "Just a minute", Toast.LENGTH_SHORT).show();
                    sendMessage(message);
                    mEditTextMessage.setText("");
                    builder = new AlertDialog.Builder(MainActivity.this);
                    View mview =  getLayoutInflater().inflate(R.layout.activity_bookid, null);
                    Author_Name = (EditText) mview.findViewById(R.id.authorname);
                    Book_Name = (EditText) mview.findViewById(R.id.bookname);
                    Search_button = (Button) mview.findViewById(R.id.Search_button);
                    builder.setView(mview);
                    dialog =  builder.create();
                    dialog.show();
                    Search_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String author= Author_Name.getText().toString();
                            String book= Book_Name.getText().toString();
                            getBookId(author, book);
                        }
                    });


                    //mListView.setSelection(mAdapter.getCount() - 1);

                }
                else if (message.equalsIgnoreCase("request book" )){

                    Toast.makeText(MainActivity.this, "Just a minute", Toast.LENGTH_SHORT).show();
                    sendMessage(message);
                    mEditTextMessage.setText("");
                    builder = new AlertDialog.Builder(MainActivity.this);
                    View mview =  getLayoutInflater().inflate(R.layout.activity_requestbook, null);
                    RBook_Name = (EditText) mview.findViewById(R.id.bookname);
                    RAuthor_Name = (EditText) mview.findViewById(R.id.authorname);
                    RPublisher_Name = (EditText) mview.findViewById(R.id.publisher_name);
                    RRequest_Btn = (Button) mview.findViewById(R.id.btn_request);
                    builder.setView(mview);
                    dialog =  builder.create();
                    dialog.show();
                    RRequest_Btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String book= RBook_Name.getText().toString();
                            String author= RAuthor_Name.getText().toString();
                            String publisher = RPublisher_Name.getText().toString();
                           // to do
                        }
                    });


                    //mListView.setSelection(mAdapter.getCount() - 1);

                }

                else if (message.equalsIgnoreCase("my fine" )) {

                    Toast.makeText(MainActivity.this, "Just a minute", Toast.LENGTH_SHORT).show();
                    sendMessage(message);
                    mEditTextMessage.setText("");
                    builder = new AlertDialog.Builder(MainActivity.this);
                    View mview = getLayoutInflater().inflate(R.layout.activity_fine, null);
                    Fine_Amt = (EditText) mview.findViewById(R.id.Fine);
                    btn_payoffline = (Button) mview.findViewById(R.id.btn_payoffline);
                    btn_payonline = (Button) mview.findViewById(R.id.btn_payonline);
                    builder.setView(mview);
                    dialog = builder.create();
                    dialog.show();
                    btn_payonline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent viewIntent =
                                    new Intent("android.intent.action.VIEW",
                                            Uri.parse("http://www.stackoverflow.com/"));
                            startActivity(viewIntent);

                        }
                    });
                }
                else if(message.equalsIgnoreCase("issue book")){
                    Toast.makeText(MainActivity.this, "Just a minute", Toast.LENGTH_SHORT).show();
                    sendMessage(message);
                    mEditTextMessage.setText("");
                    builder = new AlertDialog.Builder(MainActivity.this);
                    View mview =  getLayoutInflater().inflate(R.layout.dialog_issue, null);
                    Book_Id = (EditText) mview.findViewById(R.id.Book_Id);
                    Issue_btn = (Button) mview.findViewById(R.id.Issue_btn);
                    builder.setView(mview);
                    dialog =  builder.create();
                    dialog.show();
                    Issue_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            issueMeBooks(Integer.parseInt(Book_Id.getText().toString()));
                        }
                    });

                }

                else if(message.equalsIgnoreCase("checkout")){
                    final SharedPreferences sharedPreferences= getSharedPreferences("mypref", Context.MODE_PRIVATE);
                    String type = sharedPreferences.getString("type","");
                    sendMessage(message);
                    mEditTextMessage.setText("");
                    if(type.equals(""))
                    {
                        sendMessage("Login again.");
                    }
                    else if(type.equals("0") || type.equals("1"))
                    {
                        sendMessage("You don't have rights to checkout");
                        dialog.dismiss();
                    }
                    else if(type.equals("2")){

                        builder = new AlertDialog.Builder(MainActivity.this);
                        View mview =  getLayoutInflater().inflate(R.layout.dialog_checkout, null);
                        final EditText enroll = (EditText) mview.findViewById(R.id.enroll);
                        Button submit = (Button) mview.findViewById(R.id.submit);
                        builder.setView(mview);
                        dialog =  builder.create();
                        dialog.show();
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(enroll.getText().toString().equals(""))
                                {
                                    sendMessage("Enter username");
                                    return;
                                }

                                Call<ArrayList<BookBean>> call =retrofitClientInterface.getCheckout(enroll.getText().toString());
                                call.enqueue(new Callback<ArrayList<BookBean>>() {
                                    @Override
                                    public void onResponse(Response<ArrayList<BookBean>> response) {
                                        ArrayList<BookBean> books =response.body();
                                        if(books == null || books.size()==0)
                                        {
                                            Toast.makeText(MainActivity.this, "No Books to checkout", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                        else {


                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Toast.makeText(MainActivity.this, "Server Error. please try later.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
                else {
                    String response = chat.multisentenceRespond(mEditTextMessage.getText().toString());

                    if (TextUtils.isEmpty(message)) {
                        return;
                    }
                    sendMessage(message);
                    mimicOtherMessage(response);
                    mEditTextMessage.setText("");
                    mListView.setSelection(mAdapter.getCount() - 1);
                }
            }
        });
        //checking SD card availability

        boolean a = isSDCARDAvailable();
        //receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/hari/bots/Hari");
        boolean b = jayDir.mkdirs();
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Hari")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Hari/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Hari/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //get the working directory
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
        //Assign the AIML files to bot for processing
        bot = new Bot("Hari", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        mainFunction(args);

    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

       // mimicOtherMessage("HEllO");
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

   private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
         mAdapter.add(chatMessage);
   }
    //check SD card availability
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true : false;
    }
    //copying the file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    //Request and response of user and the bot
    public static void mainFunction (String[] args) {
        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        Timer timer = new Timer();
        String request = "Hello.";
        String response = chat.multisentenceRespond(request);

        System.out.println("Human: "+request);
        System.out.println("Robot: " + response);
    }

    public void issueMeBooks(int bookId)
    {
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        String user=sharedpreferences.getString("username",null);
        if(user!=null){
            Call<ResponseBean> call = retrofitClientInterface.issueBook(bookId,user);
            call.enqueue(new Callback<ResponseBean>() {
                @Override
                public void onResponse(Response<ResponseBean> response) {
                    ResponseBean res=response.body();
                    mimicOtherMessage(res.getMessage());
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
        else
        {
            dialog.dismiss();
            Toast.makeText(this, "Please login to issue book", Toast.LENGTH_SHORT).show();
        }

    }

    public void getBooks()
    {
        Call<ArrayList<BookBean>> retrofitUserCall =   retrofitClientInterface.getAllBooks();

        retrofitUserCall.enqueue(new Callback<ArrayList<BookBean>>() {

            @Override
            public void onResponse(Response<ArrayList<BookBean>> response) {

                ArrayList<BookBean> books =response.body();
                if(books!=null)
                {
                    res="";
                   for(int i=0;i<books.size();i++)
                   {
                       res+="Book Name:" + books.get(i).getBook_name()+"\n";
                       res+="Author Name: "+books.get(i).getAuthor()+"\n";
                       res+="Genre: "+books.get(i).getGenre()+"\n";
                       res+="Copies: "+books.get(i).getCopies();

                       if(i+1< books.size())
                       {
                           res+="\n\n\n\n";
                       }

                   }
                    mimicOtherMessage(res);
                    mListView.setSelection(mAdapter.getCount() - 1);
                }


            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode==Tools.MY_PERMISSIONS_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    result = Tools.checkPermission(MainActivity.this);
                }
                return;

        }

// book id
    private String getBookId(String author, String bookName) {

        Call<ArrayList<BookBean>> retrofitUserCall =   retrofitClientInterface.searchBooks(bookName,author);

        retrofitUserCall.enqueue(new Callback<ArrayList<BookBean>>() {
            @Override
            public void onResponse(Response<ArrayList<BookBean>> response) {
                ArrayList<BookBean> books=response.body();
                if(books==null || books.size()==0)
                {
                    return;
                }
                ans="Book ID= "+String.valueOf(books.get(0).getBook_id());
                mimicOtherMessage(ans);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MainActivity.this, "Server Error.PLease try after sometime.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        return ans;
    }

}
