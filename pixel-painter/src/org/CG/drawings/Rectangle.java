package org.CG.drawings;

import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL;
import org.CG.infrastructure.Drawing;
import org.CG.infrastructure.abstractions.Vector3;

/**
 * Rectangle drawing using two points.
 *
 * @author ldavid
 */
public class Rectangle extends Drawing {

    protected Vector3 end;
    protected byte plane;
    protected int planePosition;

    public Rectangle() {
        this(GL.GL_POLYGON);
    }

    public Rectangle(int drawingMethod) {
        super(drawingMethod);

        plane = 2;
        planePosition = 0;
    }

    @Override
    public Drawing moveTo(Vector3 v) {
        end = v.move(end.delta(start));
        start = v;

        return this;
    }

    @Override
    public Drawing updateLastCoordinate(Vector3 point) {
        end = point;
        return this;
    }

    @Override
    protected void drawShape(GL gl) {
        List<Vector3> points = new LinkedList<>();

        switch (plane) {
            case 0:
                points.add(new Vector3(planePosition, start.getY(), start.getZ()));
                points.add(new Vector3(planePosition, start.getY(), end.getZ()));
                points.add(new Vector3(planePosition, end.getY(), end.getZ()));
                points.add(new Vector3(planePosition, end.getY(), start.getZ()));
                break;

            case 1:
                points.add(new Vector3(start.getX(), planePosition, start.getZ()));
                points.add(new Vector3(end.getX(), planePosition, start.getZ()));
                points.add(new Vector3(end.getX(), planePosition, end.getZ()));
                points.add(new Vector3(start.getX(), planePosition, end.getZ()));
                break;

            case 2:
                points.add(new Vector3(start.getX(), start.getY(), planePosition));
                points.add(new Vector3(start.getX(), end.getY(), planePosition));
                points.add(new Vector3(end.getX(), end.getY(), planePosition));
                points.add(new Vector3(end.getX(), start.getY(), planePosition));
                break;
        }

        points.stream().map(p -> p.projectTo2d()).forEach(p -> {
            gl.glVertex2d(p.getX(), p.getY());
        });
    }

    @Override
    public Drawing setStart(Vector3 start) {
        super.setStart(start);
        end = start;

        return this;
    }

    public Drawing setPlane(int plane) {
        if (plane < 0 || plane > 2) {
            throw new IllegalArgumentException("Invalid plane " + plane);
        }

        this.plane = (byte) plane;

        return this;
    }

    public byte getPlane() {
        return plane;
    }

    public Drawing setPlanePosition(int planePosition) {
        this.planePosition = planePosition;

        return this;
    }
}
