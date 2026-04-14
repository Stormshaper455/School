import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class CardGameFX extends Application {
// reminder add card effects !!!!
    // add unlimited cards later
    class Card {
        String name;
        int power;
        int cost;
        String effect;

        Card(String name, int power, int cost, String effect) {
            this.name = name;
            this.power = power;
            this.cost = cost;
            this.effect = effect;
        }
    }

    class Player {
        int resolve = 2;
        ArrayList<Card> hand = new ArrayList<>();
        String[] effects = {"dM", "addTo1", "drain2", "none"};

        // double in mid, add +1 power to all secs, drain enemy power -2 in section, nothing
        /*
        if(selected.effect == "dM" && section == 2) {
            selected.power * 2;
        }
         */

        Player() {
            hand.add(new Card("card1", 2, 1, "none"));
            hand.add(new Card("card2", 2, 1, "none"));
            hand.add(new Card("card3", 5, 3, "none"));
            hand.add(new Card("card4", 7, 5, "none"));
            hand.add(new Card("card5", 5, 3, "none"));
            hand.add(new Card("card6", 2, 1, "none"));
            hand.add(new Card("card7", 2, 1, "none"));
            hand.add(new Card("card8", 2, 1, "none"));
            hand.add(new Card("card9", 2, 1, "none"));
        }

        void ranPower() {
            Random rand = new Random();

            for (int i = 5; i < hand.size(); i++) {
                int hold = rand.nextInt(10) + 1;
                hand.get(i).power = hold;

                if (hand.get(i).power <= 2) {
                    hand.get(i).cost = 0;
                } else {
                    hand.get(i).cost = hand.get(i).power - 2;
                }
            }
        }
    }

    class Section {
        int userPower = 0;
        int userPlaced = 0;
        int aiPower = 0;
        int aiPlaced = 0;
        String effect = "none";
    }

    class Board {
        int turns = 6;

        int fUser1 = 0, fAi1 = 0;
        int fUser2 = 0, fAi2 = 0;
        int fUser3 = 0, fAi3 = 0;

        int userScore = 0;
        int aiScore = 0;

        String[] effects = {"poison", "disabled2", "double", "none"};

        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();

        Player player;
        Player ai;

        void effectChooser() {
            Random rand = new Random();
            section1.effect = effects[rand.nextInt(effects.length)];
            section2.effect = effects[rand.nextInt(effects.length)];
            section3.effect = effects[rand.nextInt(effects.length)];
        }

        void boardEffects(Section section) { // effects for board
            if (section.effect.equals("poison") && turns > 2) {
                section.userPower -= 1;
                section.aiPower -= 1;
            }
            if (section.effect.equals("disabled2") && turns > 4) {
                section.userPower = -100;
                section.aiPower = -100;
            }
            if (section.effect.equals("double") && turns > 4) { // double effect
                section.userPower *= 2;
                section.aiPower *= 2;
            }
        }

        void game(Player player, Player ai) {
            this.player = player;
            this.ai = ai;
            player.ranPower();
            ai.ranPower();
            startTurn();
        }

        void start(Player player, Player ai) {
            game(player, ai);
        }

        void startTurn() {
            if (turns == 6) {
                effectChooser();
            } else {
                player.resolve += 5;
                ai.resolve += 5;
            }

            boardEffects(section1);
            boardEffects(section2);
            boardEffects(section3);
        }

        int ability(Card card, int section, int turns, Player player) {
            int power = card.power;

            if (card.effect.equals("drain2") && turns > 3) power -= 2;
            if (card.effect.equals("dM") && section == 2) power *= 2;

            if (card.effect.equals("addTo1")) {
                if (player == this.player) {
                    section1.userPower++;
                    section2.userPower++;
                    section3.userPower++;
                } else {
                    section1.aiPower++;
                    section2.aiPower++;
                    section3.aiPower++;
                }
            }
            return power;
        }
                // USER STYff
        void playerUserCard(Player player, int cardIndex, int section) {
            if (cardIndex < 0 || cardIndex >= player.hand.size()) {
                statusLabel.setText("Invalid card number.");
                return;
            }

            Card card = player.hand.get(cardIndex);

            if (section > 3 || section < 1 || player.resolve < card.cost) {
                statusLabel.setText("You dont have enough resolve");
                return;
            }

            int power = ability(card, section, turns, player);

            statusLabel.setText(card.name + " played in section " + section);
            player.resolve -= card.cost;

            if (section == 1) {
                if (player == this.player) section1.userPower += power;
                else section1.aiPower += power;
            }

            if (section == 2) {
                if (player == this.player) section2.userPower += power;
                else section2.aiPower += power;
            }

            if (section == 3) {
                if (player == this.player) section3.userPower += power;
                else section3.aiPower += power;
            }

            player.hand.remove(cardIndex);
        }

        void aiTurn() { // ill use an api add later
            boolean playedAtLeastOne = false;

            while (true) {
                int playableIndex = chooseAiCardIndex();

                if (playableIndex == -1) {
                    break;
                }

                Card aiCard = ai.hand.get(playableIndex);
                int chosenSection = chooseAiSection(aiCard);

                addPlacedCard(aiCard, chosenSection, false);
                playerUserCard(ai, playableIndex, chosenSection);
                playedAtLeastOne = true;
            }

            if (!playedAtLeastOne) {
                statusLabel.setText("AI skipped turn");
            } else {
                statusLabel.setText("AI finished its turn");
            }
        }

        int chooseAiCardIndex() {
             ///  i play highest sets
            int bestIndex = -1;
            int bestPower = -999;

            for (int i = 0; i < ai.hand.size(); i++) {
                Card c = ai.hand.get(i);
                if (ai.resolve >= c.cost && c.power > bestPower) {
                    bestPower = c.power;
                    bestIndex = i;
                }
            }

            return bestIndex;
        }

        int chooseAiSection(Card aiCard) { // ai card here need to make random later
            return chooseAiSectionC(aiCard);
        }

        int chooseAiSectionC(Card aiCard) {
            int score1 = getSectionNeed(1) + effectBonus(1, aiCard);
            int score2 = getSectionNeed(2) + effectBonus(2, aiCard);
            int score3 = getSectionNeed(3) + effectBonus(3, aiCard);

            if (score1 >= score2 && score1 >= score3) return 1;
            if (score2 >= score1 && score2 >= score3) return 2;
            return 3;
        }

        int getSectionNeed(int section) {
            if (section == 1) return section1.userPower - section1.aiPower;
            if (section == 2) return section2.userPower - section2.aiPower;
            return section3.userPower - section3.aiPower;
        }

        int effectBonus(int section, Card aiCard) {
            String effect;

            if (section == 1) effect = section1.effect;
            else if (section == 2) effect = section2.effect;
            else effect = section3.effect;

            int bonus = 0;
            // effects come and fix later
            if (effect.equals("double") && turns > 3) bonus += aiCard.power;
            if (effect.equals("poison") && turns > 3) bonus -= 1;
            if (effect.equals("disabled2") && turns <= 2) bonus -= 1000;
            if (aiCard.effect.equals("dM") && section == 2) bonus += aiCard.power;

            return bonus;
        }

        void endTurn() {
            aiTurn();
            turns--;

            if (turns == 0) {
                fUser1 = section1.userPower;
                fAi1 = section1.aiPower;
                fUser2 = section2.userPower;
                fAi2 = section2.aiPower;
                fUser3 = section3.userPower;
                fAi3 = section3.aiPower;
                win();
            } else {
                startTurn();
            }
        }

        void win() {
            userScore = 0;
            aiScore = 0;

            if (fUser1 > fAi1) userScore++;
            else if (fAi1 > fUser1) aiScore++;

            if (fUser2 > fAi2) userScore++;
            else if (fAi2 > fUser2) aiScore++;

            if (fUser3 > fAi3) userScore++;
            else if (fAi3 > fUser3) aiScore++;

            if (userScore > aiScore) {
                statusLabel.setText("You WIN!");
            } else if (aiScore > userScore) {
                statusLabel.setText("You Lose");
            } else {
                statusLabel.setText("Tie Game");
            }
        }
    }

    Player p = new Player();
    Player ai = new Player();
    Board b = new Board();

    Label topInfoLabel;
    static Label statusLabel;

    TextField cardField;
    TextField sectionField;

    HBox handBox;

    VBox section1AiBox; // dont forget for later up and down
    VBox section2AiBox;
    VBox section3AiBox;

    VBox section1UserBox;
    VBox section2UserBox;
    VBox section3UserBox;

    Label section1Info;
    Label section2Info;
    Label section3Info;

    @Override
    public void start(Stage stage) {
        Label title = new Label("Card Game");
        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f8fafc;"
        );

        topInfoLabel = new Label();
        topInfoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #dbeafe; -fx-font-weight: bold;");

        statusLabel = new Label("Game started");
        statusLabel.setStyle(
                "-fx-font-size: 16px;" + // might maker bigger
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #fef3c7;" +
                        "-fx-background-color: #7c2d12;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 10;"
        );

        cardField = new TextField();
        cardField.setPromptText("Card number");
        cardField.setPrefWidth(120);
        styleInput(cardField);

        sectionField = new TextField();
        sectionField.setPromptText("Section 1-3");
        sectionField.setPrefWidth(120);
        styleInput(sectionField);

        Button playButton = new Button("Play Card");
        Button endTurnButton = new Button("End Turn");
        styleMainButton(playButton, false); // shortcut for green
        styleMainButton(endTurnButton, true); // red

        section1Info = new Label();
        section2Info = new Label();
        section3Info = new Label();

        section1AiBox = new VBox(8);
        section2AiBox = new VBox(8);
        section3AiBox = new VBox(8);

        section1UserBox = new VBox(8);
        section2UserBox = new VBox(8);
        section3UserBox = new VBox(8);

        stylePlayArea(section1AiBox, true);
        stylePlayArea(section2AiBox, true);
        stylePlayArea(section3AiBox, true);

        stylePlayArea(section1UserBox, false);
        stylePlayArea(section2UserBox, false);
        stylePlayArea(section3UserBox, false);

        VBox sec1 = makeSectionBox("Section 1", section1AiBox, section1Info, section1UserBox);
        VBox sec2 = makeSectionBox("Section 2", section2AiBox, section2Info, section2UserBox);
        VBox sec3 = makeSectionBox("Section 3", section3AiBox, section3Info, section3UserBox);

        HBox boardBox = new HBox(20, sec1, sec2, sec3);
        boardBox.setAlignment(Pos.CENTER);
        boardBox.setPadding(new Insets(15));

        handBox = new HBox(15);
        handBox.setPadding(new Insets(10));
        handBox.setAlignment(Pos.CENTER_LEFT);

        Label handTitle = new Label("Your Hand");
        handTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        HBox controls = new HBox(10, cardField, sectionField, playButton, endTurnButton);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox bottomArea = new VBox(8, handTitle, handBox, controls);
        bottomArea.setPadding(new Insets(12));
        bottomArea.setStyle(
                "-fx-background-color: linear-gradient(to right, #1e293b, #0f172a);" +
                        "-fx-border-color: #38bdf8;" +
                        "-fx-border-width: 2 0 0 0;"
        );

        VBox topArea = new VBox(10, title, topInfoLabel, statusLabel);
        topArea.setPadding(new Insets(14));
        topArea.setStyle(
                "-fx-background-color: linear-gradient(to right, #312e81, #1d4ed8);" +
                        "-fx-border-color: #93c5fd;" +
                        "-fx-border-width: 0 0 2 0;"
        );

        BorderPane root = new BorderPane();
        root.setTop(topArea);
        root.setCenter(boardBox);
        root.setBottom(bottomArea);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0f172a, #1e293b);");

        playButton.setOnAction(e -> {
            try {
                int cardIndex = Integer.parseInt(cardField.getText()) - 1; // getting input from user
                int section = Integer.parseInt(sectionField.getText());
                // fails
                if (cardIndex < 0 || cardIndex >= b.player.hand.size()) {
                    statusLabel.setText("Invalid card number.");
                    return;
                }

                Card selected = b.player.hand.get(cardIndex);

                if (section > 3 || section < 1 || b.player.resolve < selected.cost) {
                    statusLabel.setText("You dont have enough resolve");
                    return;
                }

                addPlacedCard(selected, section, true);
                b.playerUserCard(b.player, cardIndex, section);

                updateInfo();
                updateSections();
                showHand();

                cardField.clear();
                sectionField.clear();

            } catch (Exception ex) {
                statusLabel.setText("Invalid input.");
            }
        });

        endTurnButton.setOnAction(e -> { //turn
            b.endTurn();
            updateInfo();
            updateSections();
            showHand();
        });

        Scene scene = new Scene(root, 1350, 850); // window dime might make smaller
        stage.setScene(scene);
        stage.setTitle("Card Game");
        stage.show();

        b.start(p, ai);
        showHand();
        updateInfo();
        updateSections();
    }

    VBox makeSectionBox(String titleText, VBox aiBox, Label centerInfo, VBox userBox) {
        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        Label aiLabel = new Label("AI");
        aiLabel.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: #bfdbfe;" +
                        "-fx-font-size: 15px;"
        );

        Label userLabel = new Label("You");
        userLabel.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: #fde68a;" +
                        "-fx-font-size: 15px;"
        );

        centerInfo.setAlignment(Pos.CENTER);
        centerInfo.setStyle(
                "-fx-border-color: #60a5fa;" +
                        "-fx-border-width: 2;" +
                        "-fx-background-color: linear-gradient(to bottom, #e0f2fe, #dbeafe);" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #0f172a;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;"
        );
        centerInfo.setMinHeight(90);
        centerInfo.setMaxWidth(Double.MAX_VALUE);

        VBox box = new VBox(10,
                title,
                aiLabel,
                aiBox,
                centerInfo,
                userLabel,
                userBox
        );

        box.setPrefWidth(400);
        box.setPadding(new Insets(12));
        box.setStyle(
                "-fx-border-color: #60a5fa;" +
                        "-fx-border-width: 2;" +
                        "-fx-background-color: linear-gradient(to bottom, #1e3a8a, #1e293b);" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;"
        );

        return box;
    }

    void stylePlayArea(VBox box, boolean isAiSide) {
        box.setPrefSize(360, 220);
        box.setMinSize(360, 220);
        box.setPadding(new Insets(10));
        box.setStyle(
                "-fx-border-color: " + (isAiSide ? "#60a5fa;" : "#facc15;") +
                        "-fx-background-color: " + (isAiSide ? "#dbeafe;" : "#fef9c3;") +
                        "-fx-border-width: 2;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;"
        );
    }

    void styleInput(TextField field) {
        field.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: #f8fafc;" +
                        "-fx-border-color: #60a5fa;" +
                        "-fx-border-width: 2;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );
    }

    void styleMainButton(Button button, boolean dangerStyle) {
        if (dangerStyle) {
            button.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: linear-gradient(to bottom, #ef4444, #b91c1c);" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 10 18 10 18;"
            );
        } else {
            button.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: linear-gradient(to bottom, #22c55e, #15803d);" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 10 18 10 18;"
            );
        }
    }

    Label makePlacedCard(Card c, boolean isUser) {
        Label card = new Label(
                c.name +
                        "\nP: " + c.power +
                        "  C: " + c.cost +
                        "\n" + c.effect
        );

        card.setMinSize(120, 80);
        card.setStyle(
                "-fx-border-color: " + (isUser ? "#ca8a04;" : "#2563eb;") +
                        "-fx-border-width: 2;" +
                        "-fx-background-color: " + (isUser ? "linear-gradient(to bottom, #fde68a, #facc15);" : "linear-gradient(to bottom, #bfdbfe, #60a5fa);") +
                        "-fx-text-fill: #111827;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );

        return card;
    }

    void addPlacedCard(Card c, int section, boolean isUser) {
        Label placedCard = makePlacedCard(c, isUser);

        if (section == 1) {
            if (isUser) section1UserBox.getChildren().add(placedCard);
            else section1AiBox.getChildren().add(placedCard);
        } else if (section == 2) {
            if (isUser) section2UserBox.getChildren().add(placedCard);
            else section2AiBox.getChildren().add(placedCard);
        } else if (section == 3) {
            if (isUser) section3UserBox.getChildren().add(placedCard);
            else section3AiBox.getChildren().add(placedCard);
        }
    }

    void showHand() {
        handBox.getChildren().clear();

        for (int i = 0; i < b.player.hand.size(); i++) {
            Card c = b.player.hand.get(i);

            Label card = new Label(
                    (i + 1) + ". " + c.name +
                            "\nPower: " + c.power +
                            "\nCost: " + c.cost +
                            "\nEffect: " + c.effect
            );

            card.setMinSize(145, 115);
            card.setStyle(
                    "-fx-border-color: #f59e0b;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-color: linear-gradient(to bottom, #fef08a, #facc15);" +
                            "-fx-text-fill: #111827;" +
                            "-fx-padding: 12;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-radius: 12;"
            );

            handBox.getChildren().add(card);
        }
    }

    void updateInfo() {
        topInfoLabel.setText(
                "Turns: " + b.turns +
                        "   Resolve: " + b.player.resolve +
                        "   Score: You " + b.userScore + " - AI " + b.aiScore
        );
    }

    void updateSections() {
        section1Info.setText(
                "Effect: " + b.section1.effect +
                        "\nYou: " + b.section1.userPower +
                        " | AI: " + b.section1.aiPower
        );

        section2Info.setText(
                "Effect: " + b.section2.effect +
                        "\nYou: " + b.section2.userPower +
                        " | AI: " + b.section2.aiPower
        );

        section3Info.setText(
                "Effect: " + b.section3.effect +
                        "\nYou: " + b.section3.userPower +
                        " | AI: " + b.section3.aiPower
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}