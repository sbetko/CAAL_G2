package org.baxter_academy.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import weka.classifiers.Classifier;
import weka.core.Instances;

import static android.R.attr.key;
import static org.baxter_academy.test.R.id.showResults;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** ASSIGN / LOAD RESOURCES **/
        // assigns classifier
        InputStream classifier = getResources().openRawResource(R.raw.randomforestbinarycfscsc20);

        Classifier cls = null; // TODO study SerializationHelper
        try {
            cls = (Classifier) weka.core.SerializationHelper.read(classifier);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /** Does not work
        // assign and load labeled data
        InputStreamReader labeledData = new InputStreamReader(
                getResources().openRawResource(R.raw.testlabeledbinarycfs)
        );
        BufferedReader labeledDataReader = new BufferedReader(labeledData);
        **/
        // assign unlabeled data
        Reader reader = new InputStreamReader(
                getResources().openRawResource(R.raw.testunlabeledbinarycfs)
        );

        // load unlabeled data
        Instances unlabeled = null;
        try {
            unlabeled = new Instances(
                    new BufferedReader(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** PREPARE OPERATIONS **/
        // set class attribute
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

        // create copy
        Instances labeled = new Instances(unlabeled);

        /** PERFORM CLASSIFICATION **/
        // label instances
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            double clsLabel = 0;
            try {
                clsLabel = cls.classifyInstance(unlabeled.instance(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            labeled.instance(i).setClassValue(clsLabel);
        }

        /** SAVE LABELED INSTANCE **/
        String FILENAME = "classification";
        File file = new File(FILENAME);
        BufferedWriter writer = null;
        
        
        // opens file for writing
        try {
            writer = new BufferedWriter(
                    new FileWriter(new File(getFilesDir(), FILENAME)
                    ));

        } catch (IOException e) {
            e.printStackTrace();
        }

        // writes to file
        String toWrite = labeled.toString();
        try {
            System.out.println(labeled); // debug
            if (writer != null) {
                writer.write(toWrite);
                writer.newLine();
                writer.flush();
                writer.close();
                System.out.println("file reference is not null");
            } else {
                System.out.println("file reference is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** DISPLAY **/
        /** does not work //TODO add TextView for labeled data (for comparison purposes)
        // displays test data
        String line = labeledDataReader.readLine();
        String labeledDataString;
        while (line != null) {
            line = labeledDataReader.readLine();
            labeledDataString += line;
        }
         TextView showTestData = (TextView) findViewById(showTestData);
         showTestData.setText();
        **/

        // displays prediction
        TextView showResults = (TextView) findViewById(R.id.showResults);
        showResults.setText(toWrite);
    }
}