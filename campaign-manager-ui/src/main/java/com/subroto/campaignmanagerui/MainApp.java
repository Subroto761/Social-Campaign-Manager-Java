package com.subroto.campaignmanagerui;

import com.subroto.campaignmanagerui.model.Campaign;
import com.subroto.campaignmanagerui.service.ApiService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class MainApp extends Application {

    private final ApiService apiService = new ApiService();
    private final ListView<Campaign> campaignListView = new ListView<>();
    private final TextArea detailsArea = new TextArea();
    private Campaign selectedCampaign = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Campaign Manager");

        // --- Left Panel: List of Campaigns ---
        Label listTitleLabel = new Label("Campaigns");
        listTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button newButton = new Button("➕ New Campaign");
        newButton.setOnAction(e -> showCreateCampaignDialog());

        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setOnAction(e -> loadCampaigns());

        HBox listHeaderButtons = new HBox(10, newButton, refreshButton);
        VBox leftPanel = new VBox(10, listTitleLabel, listHeaderButtons, campaignListView);
        leftPanel.setPadding(new Insets(10));

        // --- Right Panel: Campaign Details ---
        Label detailsTitleLabel = new Label("Details");
        detailsTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        detailsArea.setEditable(false);
        detailsArea.setWrapText(true);
        detailsArea.setPromptText("Select a campaign to see its details");

        Button editButton = new Button("✏️ Edit");
        editButton.setOnAction(e -> showEditCampaignDialog());

        Button deleteButton = new Button("🗑️ Delete");
        deleteButton.setOnAction(e -> deleteSelectedCampaign());

        HBox detailsButtons = new HBox(10, editButton, deleteButton);
        VBox rightPanel = new VBox(10, detailsTitleLabel, detailsArea, detailsButtons);
        VBox.setVgrow(detailsArea, Priority.ALWAYS);
        rightPanel.setPadding(new Insets(10));

        // --- Main Layout: Split Pane ---
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPanel, rightPanel);
        splitPane.setDividerPositions(0.4);

        // --- Listener for ListView Selection ---
        campaignListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedCampaign = newSelection;
            if (newSelection != null) {
                detailsArea.setText("Title: " + newSelection.getTitle() + "\n\nDescription:\n" + newSelection.getDescription());
            } else {
                detailsArea.clear();
            }
        });

        Scene scene = new Scene(splitPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadCampaigns();
    }

    private void loadCampaigns() {
        new Thread(() -> {
            try {
                List<Campaign> campaigns = apiService.getCampaigns();
                Platform.runLater(() -> {
                    campaignListView.setItems(FXCollections.observableArrayList(campaigns));
                    detailsArea.clear();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void deleteSelectedCampaign() {
        if (selectedCampaign == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Campaign: " + selectedCampaign.getTitle());
        alert.setContentText("Are you sure you want to delete this campaign? This action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    apiService.deleteCampaign(selectedCampaign.getId());
                    Platform.runLater(this::loadCampaigns);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void showCreateCampaignDialog() {
        Dialog<Campaign> dialog = new Dialog<>();
        dialog.setTitle("Create New Campaign");
        dialog.setHeaderText("Enter the details for the new campaign.");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description");

        Button aiButton = new Button("Generate with AI");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(aiButton, 2, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1, 2, 1);

        dialog.getDialogPane().setContent(grid);

        aiButton.setOnAction(e -> {
            String title = titleField.getText();
            if (!title.isEmpty()) {
                descriptionArea.setText("Generating with AI...");
                new Thread(() -> {
                    try {
                        String desc = apiService.generateDescription(title);
                        Platform.runLater(() -> descriptionArea.setText(desc));
                    } catch (Exception ex) {
                        Platform.runLater(() -> descriptionArea.setText("Error generating description."));
                    }
                }).start();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Campaign newCampaign = new Campaign();
                newCampaign.setTitle(titleField.getText());
                newCampaign.setDescription(descriptionArea.getText());
                return newCampaign;
            }
            return null;
        });

        Optional<Campaign> result = dialog.showAndWait();
        result.ifPresent(campaign -> {
            new Thread(() -> {
                try {
                    apiService.createCampaign(campaign);
                    Platform.runLater(this::loadCampaigns);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private void showEditCampaignDialog() {
        if (selectedCampaign == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Campaign Selected");
            alert.setContentText("Please select a campaign from the list to edit.");
            alert.showAndWait();
            return;
        }

        Dialog<Campaign> dialog = new Dialog<>();
        dialog.setTitle("Edit Campaign");
        dialog.setHeaderText("Edit the details for '" + selectedCampaign.getTitle() + "'");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(selectedCampaign.getTitle());
        TextArea descriptionArea = new TextArea(selectedCampaign.getDescription());

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedCampaign.setTitle(titleField.getText());
                selectedCampaign.setDescription(descriptionArea.getText());
                return selectedCampaign;
            }
            return null;
        });

        Optional<Campaign> result = dialog.showAndWait();
        result.ifPresent(campaign -> {
            new Thread(() -> {
                try {
                    apiService.updateCampaign(campaign);
                    Platform.runLater(this::loadCampaigns);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}