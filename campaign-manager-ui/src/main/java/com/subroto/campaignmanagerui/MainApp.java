package com.subroto.campaignmanagerui;

import com.subroto.campaignmanagerui.model.Campaign;
import com.subroto.campaignmanagerui.service.ApiService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {

    private ApiService apiService = new ApiService();
    private ListView<Campaign> campaignListView = new ListView<>();

    // UI elements for creating a new campaign
    private TextField titleField = new TextField();
    private TextArea descriptionArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Campaign Manager");

        // --- Layout for Creating Campaigns ---
        Label createTitleLabel = new Label("Create a New Campaign");
        createTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        titleField.setPromptText("Campaign Title");
        descriptionArea.setPromptText("Campaign Description");
        descriptionArea.setPrefHeight(100); // Make the description area taller

        Button createButton = new Button("Create Campaign");
        createButton.setOnAction(e -> createNewCampaign());


        // --- Layout for Displaying Campaigns ---
        Label listTitleLabel = new Label("Current Campaigns");
        listTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> loadCampaigns());


        // --- Main Layout ---
        VBox layout = new VBox(10); // 10 is the spacing
        layout.setPadding(new Insets(15)); // Add some padding around the window
        layout.getChildren().addAll(
            createTitleLabel,
            new Label("Title:"),
            titleField,
            new Label("Description:"),
            descriptionArea,
            createButton,
            new Label("---"), // A separator
            listTitleLabel,
            campaignListView,
            refreshButton
        );

        Scene scene = new Scene(layout, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load campaigns when the app starts
        loadCampaigns();
    }

    private void loadCampaigns() {
        try {
            List<Campaign> campaigns = apiService.getCampaigns();
            campaignListView.setItems(FXCollections.observableArrayList(campaigns));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNewCampaign() {
        // Get text from the input fields
        String title = titleField.getText();
        String description = descriptionArea.getText();

        // Basic validation: make sure fields are not empty
        if (title.isEmpty() || description.isEmpty()) {
            System.out.println("Title and Description cannot be empty.");
            // In a real app, you would show an alert dialog to the user here
            return;
        }

        // Create a new campaign object
        Campaign newCampaign = new Campaign();
        newCampaign.setTitle(title);
        newCampaign.setDescription(description);

        try {
            // Call the API to create the campaign
            apiService.createCampaign(newCampaign);

            // Clear the input fields after successful creation
            titleField.clear();
            descriptionArea.clear();

            // Refresh the list to show the new campaign
            loadCampaigns();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}