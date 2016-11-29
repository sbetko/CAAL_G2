package org.baxter_academy.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import weka.classifiers.Classifier;
import weka.core.Instances;

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

        // assigns test data
        Reader reader = new InputStreamReader(getResources().openRawResource(R.raw.testunlabeledbinarycfs));

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
        String FILENAME = "classification"; // returns as read only fs!
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
        assert writer != null;
        try {
            System.out.println(labeled); // debug
            //TextView text = null;
            //text.setText(toWrite);
            if (writer != null) {
                writer.write(toWrite);
                writer.newLine();
                writer.flush();
                writer.close();
                System.out.println("file reference is not null");
            } else {
                System.out.println("working as not intended");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

/**
        FileOutputStream writer = null;

        // opens file for writing
        try {
            writer = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // writes to file
        try {
            System.out.println(labeled); // debug
            writer.write(labeled);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 **/
    }
}