package com.example.javafx_veterinariasegundointento;

import com.example.javafx_veterinariasegundointento.dao.MascotaDAO;
import com.example.javafx_veterinariasegundointento.model.Gato;
import com.example.javafx_veterinariasegundointento.model.Mascota;
import com.example.javafx_veterinariasegundointento.model.Perro;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {

    @FXML private ComboBox<String> cmbEspecie;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtPeso;
    @FXML private TextField txtCostoBase;
    @FXML private TextField txtRaza;
    @FXML private CheckBox chkVacunado;
    @FXML private CheckBox chkIndoor;
    @FXML private Label lblMensaje;

    @FXML private TableView<Mascota> tblMascotas;
    @FXML private TableColumn<Mascota, String> colEspecie;
    @FXML private TableColumn<Mascota, String> colNombre;
    @FXML private TableColumn<Mascota, Integer> colEdad;
    @FXML private TableColumn<Mascota, String> colPeso;
    @FXML private TableColumn<Mascota, String> colCostoBase;
    @FXML private TableColumn<Mascota, String> colDetalle;
    @FXML private TableColumn<Mascota, String> colCostoFinal;

    private final ObservableList<Mascota> listaMascotas = FXCollections.observableArrayList();
    private final MascotaDAO mascotaDAO = new MascotaDAO();

    @FXML
    public void initialize() {
        // 1. Llenar ComboBox y dejar "Perro" por defecto
        cmbEspecie.getItems().addAll("Perro", "Gato");
        cmbEspecie.setValue("Perro");
        onEspecieChanged();

        // 2. Enlazar cada columna con los atributos de Mascota
        colEspecie.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() instanceof Perro ? "Perro" : "Gato"));
        colNombre.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNombre()));
        colEdad.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getEdad()));
        colPeso.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("%.1f kg", cell.getValue().getPeso())));
        colCostoBase.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("$%.0f", cell.getValue().getCostoBase())));

        colDetalle.setCellValueFactory(cell -> {
            Mascota m = cell.getValue();
            if (m instanceof Perro p) {
                return new SimpleStringProperty("Raza: " + p.getRaza() + (p.isVacunado() ? " (Vacunado)" : " (Sin vacunar)"));
            } else if (m instanceof Gato g) {
                return new SimpleStringProperty(g.isIndoor() ? "Indoor" : "Exterior");
            }
            return new SimpleStringProperty("");
        });

        colCostoFinal.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("$%.0f", cell.getValue().calcularCostoFinal())));

        // 3. Conectar la lista observable a la tabla y cargar los JSON
        tblMascotas.setItems(listaMascotas);
        try {
            listaMascotas.addAll(mascotaDAO.cargarTodos());
        } catch (Exception e) {
            mostrarMensaje("Error al cargar datos: " + e.getMessage(), true);
        }
    }

    @FXML
    public void onEspecieChanged() {
        boolean esPerro = "Perro".equals(cmbEspecie.getValue());
        txtRaza.setDisable(!esPerro);
        chkVacunado.setDisable(!esPerro);
        chkIndoor.setDisable(esPerro);

        if (esPerro) {
            chkIndoor.setSelected(false);
        } else {
            txtRaza.clear();
            chkVacunado.setSelected(false);
        }
    }

    @FXML
    public void onRegistrarClick() {
        try {
            String especie = cmbEspecie.getValue();
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarMensaje("Debes ingresar el nombre de la mascota.", true);
                return;
            }

            int edad = Integer.parseInt(txtEdad.getText().trim());
            double peso = Double.parseDouble(txtPeso.getText().trim());
            double costoBase = Double.parseDouble(txtCostoBase.getText().trim());

            if (edad < 0 || peso <= 0 || costoBase <= 0) {
                mostrarMensaje("Ingresa una edad válida, peso y costo base mayores a 0.", true);
                return;
            }

            Mascota nuevaMascota;
            if ("Perro".equals(especie)) {
                String raza = txtRaza.getText().trim();
                if (raza.isEmpty()) {
                    mostrarMensaje("Debes ingresar la raza del perro.", true);
                    return;
                }
                boolean vacunado = chkVacunado.isSelected();
                nuevaMascota = new Perro(nombre, edad, peso, costoBase, raza, vacunado);
            } else {
                boolean indoor = chkIndoor.isSelected();
                nuevaMascota = new Gato(nombre, edad, peso, costoBase, indoor);
            }

            listaMascotas.add(nuevaMascota);
            mascotaDAO.guardarTodos(listaMascotas);
            limpiarFormulario();
            mostrarMensaje("Mascota registrada correctamente.", false);

        } catch (NumberFormatException e) {
            mostrarMensaje("Edad debe ser entero; Peso y Costo Base números válidos.", true);
        } catch (Exception e) {
            mostrarMensaje("Error al guardar: " + e.getMessage(), true);
        }
    }

    @FXML
    public void onEliminarClick() {
        Mascota seleccionada = tblMascotas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Selecciona una mascota en la tabla para eliminar.", true);
            return;
        }

        try {
            listaMascotas.remove(seleccionada);
            mascotaDAO.guardarTodos(listaMascotas);
            mostrarMensaje("Mascota eliminada correctamente.", false);
        } catch (Exception e) {
            mostrarMensaje("Error al actualizar el archivo: " + e.getMessage(), true);
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtEdad.clear();
        txtPeso.clear();
        txtCostoBase.clear();
        txtRaza.clear();
        chkVacunado.setSelected(false);
        chkIndoor.setSelected(false);
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}