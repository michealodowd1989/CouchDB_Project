package ie.ittralee.micheal.mycontactsappwithcouchdb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.*;
import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button createButton, readButton, updateButton, deleteButton, queryButton;
    private Manager myDatabaseManager;
    private Database myDatabase;
    private Document documentRetrieved,document;
    private com.couchbase.lite.View allContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencing my buttons in UI to Button instance
        createButton = (Button) findViewById(R.id.createButton);
        readButton = (Button) findViewById(R.id.readButton);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        queryButton = (Button) findViewById(R.id.queryButton);
        addButtonListeners();
    }
    //addButtonListeners() adds all listeners to buttons so when one is selected a specific method will be called.
    private void addButtonListeners() {
        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                document = createContact();
                textView.setText("\nDocument Created..!!\n\n" + "Id: " +  document.getId() + "\n\nRev:" + document.getCurrentRevisionId());
            }
        });

        readButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                textView.setText("\n\nRead");
                documentRetrieved = viewContact();
                displayContatDetails();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                textView.setText("\n\nDocument Delete..!!\n");
                deleteContact();
                textView.append("\n\nId: " + documentRetrieved.getId());
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateContact();
                textView.setText("\n\nUpdate");
                displayContatDetails();
            }
        });

        queryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                textView.setText("\n\nQuery Number Of Contacts: ");
                createView();
                queryView();
            }
        });
    }

    private Manager DatabaseManager(){
        Manager myDatabaseManager = null;
        try {
            myDatabaseManager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            return myDatabaseManager;
        } catch (IOException e) {
            textView.setText(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Database createDatabase(Manager myDatabaseManager) {
        Database database = null;
        try {
            database = myDatabaseManager.getDatabase("contacts");
            return database;
        } catch (CouchbaseLiteException e) {
            textView.setText(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void displayContatDetails() {
        for(Map.Entry<String,Object> p : documentRetrieved.getProperties().entrySet()){
            textView.append("\n\n\n" + p.getKey() + " " + p.getValue());
        }
    }

    private Document createContact() {
        myDatabaseManager = DatabaseManager();
        if (myDatabaseManager != null) {
            myDatabase = createDatabase(myDatabaseManager);
        }
        Map<String, Object> contact = new HashMap<String, Object>();
        contact.put("name: ", "Micheal O Dowd");
        contact.put("occupation: ", "Software Engineer");
        Document document = myDatabase.createDocument();

        try {
            document.putProperties(contact);
        } catch (CouchbaseLiteException e) {
            textView.setText("\n\nUnable create document\n");
            e.printStackTrace();
            return null;
        }
        return document;
    }

    private Document viewContact(){
        try {
            return myDatabase.getDocument(document.getId());
        } catch (Exception e) {
            textView.setText("\n\nDocument could not be found\n");
            e.printStackTrace();
        }
        return null;
    }

    private void updateContact() {
            Map<String, Object> contactDetails = new HashMap<String, Object>();
            contactDetails.putAll(documentRetrieved.getProperties());
            Address address = new Address("Rose Lodge","The Kerries","Tralee","Co.Kerry","102244");
            contactDetails.put("address : ", address);
            String[] contactsNumbers = {"087-26335894", "06671-54548", "086-15451515"};
            //Had to convert array to String as it was not displaying for me
            contactDetails.put("contacts", Arrays.toString(contactsNumbers));
        try {
            documentRetrieved.putProperties(contactDetails);
        } catch (CouchbaseLiteException e) {
            textView.setText("\n\nUnable to update document\n");
            e.printStackTrace();
        }
    }

    private void deleteContact() {
        try {
                documentRetrieved.delete();
        } catch (CouchbaseLiteException e) {
                textView.setText("\n\nUnable delete document\n");
                e.printStackTrace();
            }
    }

    private void createView() {
        allContacts = myDatabase.getView("contacts");
        if (allContacts.getMap() == null) {
            allContacts.setMapReduce(new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    Object object = document.get("contacts");
                    String stringOfNumbers = object.toString();
                    String numbersAsAString ="";
                    for(int i=0;i<stringOfNumbers.length();i++){
                        char ch = stringOfNumbers.charAt(i);
                        String value = ch+"";
                            if(!value.equals("[") && !value.equals("]") && !value.equals(",")){
                                  numbersAsAString +=value;
                               }
                            }
                    textView.append("\n\nList Of Numbers: \n");
                    String[] numbers = numbersAsAString.split(" ");
                         for(int i=0;i<=numbers.length-1;i++){
                             emitter.emit(document.get("name: "), 1);
                             textView.append("\n" + (i + 1)+  ": " + numbers[i] + "\n");
                           }
                         }
                           }, new Reducer() {
                                         @Override
                                         public Object reduce(List<Object> keys, List<Object> values, boolean reReduce) {
                                             return "\n\nName: " + keys.get(0) + "\nNo. Contacts: " + values.size();
                                         }
                                     }, "1");
        }
    }

    private void queryView() {
        Query myQuery = allContacts.createQuery();

        try{
            QueryEnumerator enumerator =null;
            enumerator = myQuery.run();
            String value="";
            for (Iterator<QueryRow> iterator = enumerator; iterator.hasNext();){
                QueryRow queryRow = iterator.next();
                value = queryRow.getValue() + "";
            }
             textView.append(value);

        }catch (CouchbaseLiteException e){
            e.printStackTrace();
        }
    }
}