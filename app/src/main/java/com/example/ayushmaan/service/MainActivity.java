package com.example.ayushmaan.service;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        //Using the Conversation Service
        final ConversationService myConversationService =
                new ConversationService(
                        "2018-07-10",
                        getString(R.string.username),
                        getString(R.string.password)
                );

        final TextView conversation = findViewById(R.id.conversation);
        final EditText userInput = findViewById(R.id.user_input);

        //printing the user input
        final String inputText = userInput.getText().toString();
        conversation.append(
                Html.fromHtml("<p style=\"background-color:blue;\" align=\"right\"><b>You:</b> "
                        + inputText + "&nbsp;</p>")
        );

        // Optionally, clear editText, the user input
        userInput.setText("");

        //To send the message to IBM Watson Assistant (Conversion Service)
        MessageRequest request = new MessageRequest.Builder()
                .inputText(inputText)
                .build();

        //Send finally and have a response
        myConversationService
                .message(getString(R.string.workspace), request)
                .enqueue(new ServiceCallback<MessageResponse>() {
                    @Override
                    public void onResponse(MessageResponse response) {
                        // More code here
                        final String outputText = response.getText().get(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                conversation.append(
                                        Html.fromHtml("<p style=\"background-color:grey;\" align= \"left\"><b>Bot:</b> " +
                                                outputText + "&nbsp;</p>")
                                );
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {}
                });

        //To set the auto-scroll down
        final ScrollView scrollview = findViewById(R.id.scrolldown);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

}