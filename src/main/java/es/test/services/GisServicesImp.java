package es.test.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.inject.Singleton;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import es.test.geo.World;
import es.test.resources.MapResource;

@Singleton
public class GisServicesImp implements GisServices {

    private Iterable<World> data;
    private final Object lock = new Object();

    public Iterable<World> getData() {
        // if (data == null) {
        // synchronized (lock) {
        // if (data == null) {
        String file = "CTA_-_Map_of_Fare_Media_Sales_Outlets__deprecated_August_2014_.csv";
        data = loadData(file);
        // }
        // }
        // }
        return data;
    }

    public Iterable<World> loadData(String path) {
        ArrayList<World> points = new ArrayList<>();

        /*
         * try { URL res = MapResource.class.getClassLoader().getResource(path); File file = Paths.get(res.toURI()).toFile();
         * 
         * try (Reader in = new FileReader(file); // CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader())) { for (CSVRecord record : parser) { try { double lat = Double.parseDouble(record.get("LAT")); double lon =
         * Double.parseDouble(record.get("LON")); points.add(new World(lat, lon)); } catch (NullPointerException | NumberFormatException e) { e.printStackTrace(); } } } catch (IOException e) { e.printStackTrace(); } } catch
         * (URISyntaxException e) { return points; }
         */

        // Fuente de los Galápagos
        points.add(new World(40.41866, -3.68552));

        // Fuente de la Alcachofa
        points.add(new World(40.41544, -3.68427));

        // Fuente del Ángel Caido
        points.add(new World(40.41105, -3.68254));

        // Rosaleda del Parque del El Retiro
        points.add(new World(40.4105, -3.68023));

        // Monumento a la República de Cuba
        points.add(new World(40.41934, -3.68154));

        // Monumento a Alfonso XII
        points.add(new World(40.41733, -3.68306));

        return points;
    }
}
