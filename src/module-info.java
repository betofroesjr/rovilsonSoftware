module br.edu.ufu.doutorado.pca {
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.media;
	requires javafx.controls;
	exports br.edu.ufu.doutorado.pca.view;
	exports br.edu.ufu.doutorado.pca.view.fxml;
	opens br.edu.ufu.doutorado.pca.view to javafx.graphics;
	opens br.edu.ufu.doutorado.pca.view.fxml to javafx.fxml;
}