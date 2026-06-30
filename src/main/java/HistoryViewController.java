import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HistoryViewController {
    @FXML
    public ListView<String> historyList;

    @FXML
    private void clearHistory() {
        historyList.getItems().clear();
        UserHistoryUtil.clearHistory();
    }
}
