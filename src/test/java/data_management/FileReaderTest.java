package data_management;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileReaderTest {
    @Test
    public void testFileReaderCholesterol() {
        FileDataReader reader = new FileDataReader("patientData/Cholesterol.txt");
        readFile(reader);
    }

    @Test
    public void testFileReaderDiastolic() {
        FileDataReader reader = new FileDataReader("patientData/DiastolicPressure.txt");
        readFile(reader);
    }

    @Test
    public void testFileReaderECG() {
        FileDataReader reader = new FileDataReader("patientData/ECG.txt");
        readFile(reader);
    }

    @Test
    public void testFileReaderRedBloodCells() {
        FileDataReader reader = new FileDataReader("patientData/RedBloodCells.txt");
        readFile(reader);
    }

    @Test
    public void testFileReaderSaturation() {
        FileDataReader reader = new FileDataReader("patientData/Saturation.txt");
        readFile(reader);
    }

    @Test
    public void testFileReaderSystolicPressure() {
        FileDataReader reader = new FileDataReader("patientData/SystolicPressure.txt");
        readFile(reader);
    }

    @Test
    public void testFileReaderWhiteBloodCells() {
        FileDataReader reader = new FileDataReader("patientData/WhiteBloodCells.txt");
        readFile(reader);
    }

    private static void readFile(FileDataReader reader) {
        DataStorage storage = new DataStorage();
        try {
            reader.readData(storage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
