package org.baxter_academy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
        InputStream classifier = getResources().openRawResource(R.raw.randomforest);

        Classifier cls = null; // TO-DO study SerializationHelper
        try {
            cls = (Classifier) weka.core.SerializationHelper.read(classifier);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // assigns test data
        Reader reader = new InputStreamReader(getResources().openRawResource(R.raw.testunlabeled));

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
        String FILENAME = "classification.txt";
        BufferedWriter writer = null;

        // opens file for writing
        try {
            writer = new BufferedWriter(
                    new FileWriter(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // writes to file
        try {
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}