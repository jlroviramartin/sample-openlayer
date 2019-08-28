package es.test.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import es.test.geo.GeoUtils;
import es.test.geo.Pixel;
import es.test.geo.Tile;
import es.test.geo.TileSystem;
import es.test.geo.WorldBox;
import es.test.services.GisServices;
import no.ecc.vectortile.VectorTileEncoder;

import static java.util.stream.StreamSupport.stream;

@Path("/map")
public class MapResource {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private final GisServices gisServices;

    @Inject
    public MapResource(GisServices gisServices) {
        this.gisServices = gisServices;
    }

    @GET
    @Path("/{z}/{y}/{x}.mvt")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getGeoPoints(@Context HttpServletRequest request,
            @PathParam("z") int levelOfDetail, @PathParam("y") int y, @PathParam("x") int x) {

        TileSystem tileSystem = new TileSystem();
        WorldBox worldBox = tileSystem.getBox(new Tile(x, y), levelOfDetail);

        VectorTileEncoder encoder = new VectorTileEncoder();

        System.out.println("" + worldBox);

        Coordinate[] array = stream(gisServices.getData().spliterator(), true)
                .filter(pt -> GeoUtils.contains(worldBox, pt))
                .map(pt -> {
                    Pixel pixel = tileSystem.latLongToPixelXY(pt, levelOfDetail);
                    Pixel origin = tileSystem.getOrigin(pixel);
                    int xLocal = pixel.x - origin.x;
                    int yLocal = pixel.y - origin.y;
                    return new Coordinate(xLocal, yLocal);
                })
                .toArray(sz -> new Coordinate[sz]);

        Geometry geometry = geometryFactory.createMultiPointFromCoords(array);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("color", "green");
        encoder.addFeature("POINTS", attributes, geometry);

        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                output.write(encoder.encode());
                output.flush();
            }
        };

        return Response.ok(fileStream, "image/mvt")
                .header("content-disposition", "attachment; filename = map.pbf")
                .build();
    }

    private String showParams(HttpServletRequest request) {
        StringBuffer buff = new StringBuffer();
        for (String name : new IterableEnumeration<String>(request.getParameterNames())) {
            for (String value : Arrays.asList(request.getParameterValues(name))) {
                buff.append("  " + name + " = " + value + "\r\n");
            }
        }
        return buff.toString();
    }
}
