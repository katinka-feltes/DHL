/**
 * The main module of the dhl application.
 */
module dhl {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;

    opens dhl.view;
    exports dhl.view;
}
