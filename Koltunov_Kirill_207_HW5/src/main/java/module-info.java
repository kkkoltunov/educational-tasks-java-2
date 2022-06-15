module ru.hse.cs.jigsaw {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens ru.hse.cs.jigsaw to javafx.fxml;
    exports ru.hse.cs.jigsaw;
}