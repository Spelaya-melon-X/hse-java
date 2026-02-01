package hse.java.practice.task1;


import java.util.Arrays;

/**
 * Необходимо реализовать интерфейс Cube
 * При повороте передней грани, меняются верх низ право и лево
 */


public class RubiksCube implements  Cube {

    private static final int EDGES_COUNT = 6;

    private final Edge[] edges = new Edge[EDGES_COUNT];

    /**
     * Создать валидный собранный кубик
     * грани разместить по ордеру в енуме цветов
     * грань 0 -> цвет 0
     * грань 1 -> цвет 1
     * ...
     */



    public RubiksCube() {
        CubeColor[] colors = CubeColor.values();
//        EdgePosition[] edgePositions = EdgePosition.values();
        for (int i = 0; i < 6; i++) {
            edges[i] = new Edge(colors[i]);

        }
    }



    CubeColor[][] turn(RotateDirection direction , EdgePosition edgePosition) {
        CubeColor[][] parts = new CubeColor[3][3] ;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (direction) {
                    case CLOCKWISE:
                        parts[i][j] = edges[edgePosition.getEdgeNumber()].getParts()[2 - j][i];
                        break;

                    case COUNTERCLOCKWISE:
                        parts[i][j] = edges[edgePosition.getEdgeNumber()].getParts()[j][2 - i];
                        break;
                }
            }
        }
        return parts;
    }

    private CubeColor[] reverse(CubeColor[] arr) {
        CubeColor[] result = new CubeColor[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }
        return result;
    }




    @Override
    public void up(RotateDirection direction) {


        CubeColor[] leftColorEdge = Arrays.copyOf(edges[EdgePosition.LEFT.getEdgeNumber()].getPartsLine(0) ,edges[EdgePosition.LEFT.getEdgeNumber()].getPartsLine(0).length );   // red
        CubeColor[] rightColorEdge = Arrays.copyOf(edges[EdgePosition.RIGHT.getEdgeNumber()].getPartsLine(0),edges[EdgePosition.RIGHT.getEdgeNumber()].getPartsLine(0).length );  //orange
        CubeColor[] frontColorEdge = Arrays.copyOf(edges[EdgePosition.FRONT.getEdgeNumber()].getPartsLine(0) ,edges[EdgePosition.FRONT.getEdgeNumber()].getPartsLine(0) .length ) ;  //blue
        CubeColor[] backColorEdge = Arrays.copyOf(edges[EdgePosition.BACK.getEdgeNumber()].getPartsLine(0) ,edges[EdgePosition.FRONT.getEdgeNumber()].getPartsLine(0) .length ) ;  // green

        switch (direction) {
            case COUNTERCLOCKWISE :
                edges[ EdgePosition.FRONT.getEdgeNumber() ].setPartsLine(leftColorEdge , 0 );
                edges[ EdgePosition.RIGHT.getEdgeNumber() ].setPartsLine(frontColorEdge , 0 );
                edges[ EdgePosition.BACK.getEdgeNumber() ].setPartsLine(rightColorEdge, 0 );
                edges[ EdgePosition.LEFT.getEdgeNumber() ].setPartsLine(backColorEdge , 0 );
                break;

            case CLOCKWISE:
                edges[ EdgePosition.FRONT.getEdgeNumber() ].setPartsLine(rightColorEdge, 0 );
                edges[ EdgePosition.RIGHT.getEdgeNumber() ].setPartsLine(backColorEdge, 0 );
                edges[ EdgePosition.BACK.getEdgeNumber() ].setPartsLine(leftColorEdge, 0 );
                edges[ EdgePosition.LEFT.getEdgeNumber() ].setPartsLine(frontColorEdge, 0 );
                break ;

        }

        CubeColor[][] parts = turn(direction , EdgePosition.UP);
        edges[EdgePosition.UP.getEdgeNumber()].setParts(parts);
    }

    @Override
    public void down(RotateDirection direction) {

        CubeColor[] leftColorEdge = Arrays.copyOf(edges[EdgePosition.LEFT.getEdgeNumber()].getPartsLine(2),edges[EdgePosition.LEFT.getEdgeNumber()].getPartsLine(2).length );   // red
        CubeColor[] rightColorEdge = Arrays.copyOf(edges[EdgePosition.RIGHT.getEdgeNumber()].getPartsLine(2) ,edges[EdgePosition.RIGHT.getEdgeNumber()].getPartsLine(2).length );  //orange
        CubeColor[] frontColorEdge = Arrays.copyOf(edges[EdgePosition.FRONT.getEdgeNumber()].getPartsLine(2) ,edges[EdgePosition.FRONT.getEdgeNumber()].getPartsLine(2) .length ) ;  //blue
        CubeColor[] backColorEdge = Arrays.copyOf(edges[EdgePosition.BACK.getEdgeNumber()].getPartsLine(2) ,edges[EdgePosition.FRONT.getEdgeNumber()].getPartsLine(2).length ) ;  // green


        switch (direction) { // TODO : можно вынести
            case CLOCKWISE :
                edges[ EdgePosition.FRONT.getEdgeNumber() ].setPartsLine(leftColorEdge ,2);
                edges[ EdgePosition.RIGHT.getEdgeNumber() ].setPartsLine(frontColorEdge,2);
                edges[ EdgePosition.BACK.getEdgeNumber() ].setPartsLine(rightColorEdge,2);
                edges[ EdgePosition.LEFT.getEdgeNumber() ].setPartsLine(backColorEdge,2);
                break ;

            case COUNTERCLOCKWISE:
                edges[ EdgePosition.FRONT.getEdgeNumber() ].setPartsLine(rightColorEdge,2);
                edges[ EdgePosition.RIGHT.getEdgeNumber() ].setPartsLine(backColorEdge,2);
                edges[ EdgePosition.BACK.getEdgeNumber() ].setPartsLine(leftColorEdge,2);
                edges[ EdgePosition.LEFT.getEdgeNumber() ].setPartsLine(frontColorEdge,2);
                break ;

        }

        CubeColor[][] parts = turn(direction , EdgePosition.DOWN) ;
        edges[EdgePosition.DOWN.getEdgeNumber()].setParts(parts);
    }

    @Override
    public void left(RotateDirection direction) {
        CubeColor[] upCol = Arrays.copyOf(edges[EdgePosition.UP.getEdgeNumber()].getPartsRow(0), 3);
        CubeColor[] frontCol = Arrays.copyOf(edges[EdgePosition.FRONT.getEdgeNumber()].getPartsRow(0), 3);
        CubeColor[] downCol = Arrays.copyOf(edges[EdgePosition.DOWN.getEdgeNumber()].getPartsRow(0), 3);
        CubeColor[] backColRaw = edges[EdgePosition.BACK.getEdgeNumber()].getPartsRow(2); // Столбец 2 BACK
        CubeColor[] backCol = reverse(Arrays.copyOf(backColRaw, 3));

        switch (direction) {
            case CLOCKWISE:
                edges[EdgePosition.FRONT.getEdgeNumber()].setPartsRow(upCol, 0);
                edges[EdgePosition.DOWN.getEdgeNumber()].setPartsRow(frontCol, 0);
                edges[EdgePosition.BACK.getEdgeNumber()].setPartsRow(reverse(downCol), 2);
                edges[EdgePosition.UP.getEdgeNumber()].setPartsRow(backCol, 0);
                break;
            case COUNTERCLOCKWISE:
                edges[EdgePosition.FRONT.getEdgeNumber()].setPartsRow(downCol, 0);
                edges[EdgePosition.DOWN.getEdgeNumber()].setPartsRow(reverse(backColRaw), 0);
                edges[EdgePosition.BACK.getEdgeNumber()].setPartsRow(reverse(upCol), 2);
                edges[EdgePosition.UP.getEdgeNumber()].setPartsRow(frontCol, 0);
                break;
        }

        edges[EdgePosition.LEFT.getEdgeNumber()].setParts(turn(direction, EdgePosition.LEFT));
    }

    @Override
    public void right(RotateDirection direction) {
        // Копируем ДО изменений (столбец 2 для всех, кроме BACK — там столбец 0)
        CubeColor[] upCol = Arrays.copyOf(edges[EdgePosition.UP.getEdgeNumber()].getPartsRow(2), 3);
        CubeColor[] frontCol = Arrays.copyOf(edges[EdgePosition.FRONT.getEdgeNumber()].getPartsRow(2), 3);
        CubeColor[] downCol = Arrays.copyOf(edges[EdgePosition.DOWN.getEdgeNumber()].getPartsRow(2), 3);
        CubeColor[] backColRaw = edges[EdgePosition.BACK.getEdgeNumber()].getPartsRow(0); // Столбец 0 EdgePosition.BACK
        CubeColor[] backCol = reverse(Arrays.copyOf(backColRaw, 3)); // Инвертируем из-за ориентации

        switch (direction) {
            case COUNTERCLOCKWISE:
                edges[EdgePosition.FRONT.getEdgeNumber()].setPartsRow(upCol, 2);
                edges[EdgePosition.DOWN.getEdgeNumber()].setPartsRow(frontCol, 2);
                edges[EdgePosition.BACK.getEdgeNumber()].setPartsRow(reverse(downCol), 0); // Инвертируем при записи
                edges[EdgePosition.UP.getEdgeNumber()].setPartsRow(backCol, 2);
                break;
            case CLOCKWISE:
                edges[EdgePosition.FRONT.getEdgeNumber()].setPartsRow(downCol, 2);
                edges[EdgePosition.DOWN.getEdgeNumber()].setPartsRow(reverse(backColRaw), 2);
                edges[EdgePosition.BACK.getEdgeNumber()].setPartsRow(reverse(upCol), 0);
                edges[EdgePosition.UP.getEdgeNumber()].setPartsRow(frontCol, 2);
                break;
        }

        edges[EdgePosition.RIGHT.getEdgeNumber()].setParts(turn(direction, EdgePosition.RIGHT));
    }

    @Override
    public void front(RotateDirection direction) {

        CubeColor[] upColorEdge = Arrays.copyOf(edges[EdgePosition.UP.getEdgeNumber()].getPartsLine(2),edges[EdgePosition.UP.getEdgeNumber()].getPartsLine(2).length );   // red
        CubeColor[] downColorEdge = Arrays.copyOf(edges[EdgePosition.DOWN.getEdgeNumber()].getPartsLine(0),edges[EdgePosition.DOWN.getEdgeNumber()].getPartsLine(0).length );   // red
        CubeColor[] leftColorEdge = Arrays.copyOf(edges[EdgePosition.LEFT.getEdgeNumber()].getPartsRow(2),edges[EdgePosition.LEFT.getEdgeNumber()].getPartsRow(2).length );   // red
        CubeColor[] rightColorEdge = Arrays.copyOf(edges[EdgePosition.RIGHT.getEdgeNumber()].getPartsRow(0),edges[EdgePosition.RIGHT.getEdgeNumber()].getPartsRow(0).length );   // red

        switch (direction) { // TODO : можно вынести
            case COUNTERCLOCKWISE :
                edges[ EdgePosition.UP.getEdgeNumber() ].setPartsLine(rightColorEdge,2);
                edges[ EdgePosition.LEFT.getEdgeNumber() ].setPartsRow(upColorEdge , 2);
                edges[ EdgePosition.DOWN.getEdgeNumber() ].setPartsLine(leftColorEdge , 0);
                edges[ EdgePosition.RIGHT.getEdgeNumber() ].setPartsRow(downColorEdge , 0);
                break ;


            case CLOCKWISE:
                edges[ EdgePosition.UP.getEdgeNumber() ].setPartsLine(leftColorEdge ,2 );
                edges[ EdgePosition.LEFT.getEdgeNumber() ].setPartsRow(downColorEdge , 2);
                edges[ EdgePosition.DOWN.getEdgeNumber() ].setPartsLine(rightColorEdge , 0);
                edges[ EdgePosition.RIGHT.getEdgeNumber() ].setPartsRow(upColorEdge , 0);
                break ;

        }

        CubeColor[][] parts = turn(direction , EdgePosition.FRONT) ;
        edges[EdgePosition.FRONT.getEdgeNumber()].setParts(parts);;

    }

    @Override
    public void back(RotateDirection direction) {
        // Сначала копируем смежные элементы!
        CubeColor[] upEdge = Arrays.copyOf(edges[EdgePosition.UP.getEdgeNumber()].getPartsLine(0), 3);
        CubeColor[] downEdge = Arrays.copyOf(edges[EdgePosition.DOWN.getEdgeNumber()].getPartsLine(2), 3);
        CubeColor[] leftEdge = Arrays.copyOf(edges[EdgePosition.LEFT.getEdgeNumber()].getPartsRow(0), 3);
        CubeColor[] rightEdge = Arrays.copyOf(edges[EdgePosition.RIGHT.getEdgeNumber()].getPartsRow(2), 3);

        switch (direction) {
            case CLOCKWISE:
                edges[EdgePosition.UP.getEdgeNumber()].setPartsLine(reverse(rightEdge), 0);
                edges[EdgePosition.LEFT.getEdgeNumber()].setPartsRow(reverse(upEdge), 0);
                edges[EdgePosition.DOWN.getEdgeNumber()].setPartsLine(reverse(leftEdge), 2);
                edges[EdgePosition.RIGHT.getEdgeNumber()].setPartsRow(reverse(downEdge), 2);
                break;
            case COUNTERCLOCKWISE:
                edges[EdgePosition.UP.getEdgeNumber()].setPartsLine(reverse(leftEdge), 0);
                edges[EdgePosition.LEFT.getEdgeNumber()].setPartsRow(reverse(downEdge), 0);
                edges[EdgePosition.DOWN.getEdgeNumber()].setPartsLine(reverse(rightEdge), 2);
                edges[EdgePosition.RIGHT.getEdgeNumber()].setPartsRow(reverse(upEdge), 2);
                break;
        }

        // Только теперь поворачиваем саму грань
        edges[EdgePosition.BACK.getEdgeNumber()].setParts(turn(direction, EdgePosition.BACK));
    }

    public Edge[] getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < EDGES_COUNT; i++) {
            Edge edge = edges[i];
            result += "\nNew Edge : " + Edge.getEdgeColor(edge)+ " \n";
            CubeColor[][] colorEdge = edge.getParts();
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    result += " " +  colorEdge[j][k] + " " ;
                }
                result += "\n";
            }

        }
        return result;
    }



    public static void main(String[] args) {
        RubiksCube rubiksCube = new RubiksCube();
        CubeColor[][] test = new CubeColor[][] {
                {CubeColor.RED , CubeColor.BLUE , CubeColor.ORANGE} ,
                {CubeColor.RED , CubeColor.BLUE , CubeColor.ORANGE} ,
                {CubeColor.RED , CubeColor.BLUE , CubeColor.ORANGE}
        };
//        rubiksCube.edges[0] = new Edge(test);
//        rubiksCube.up(RotateDirection.CLOCKWISE);
//        rubiksCube.up(RotateDirection.COUNTERCLOCKWISE);
//
//        rubiksCube.down(RotateDirection.CLOCKWISE);
//        rubiksCube.down(RotateDirection.COUNTERCLOCKWISE);
//
//        rubiksCube.right(RotateDirection.COUNTERCLOCKWISE);
//        rubiksCube.right(RotateDirection.CLOCKWISE);
//
//        rubiksCube.left(RotateDirection.COUNTERCLOCKWISE);
//        rubiksCube.left(RotateDirection.CLOCKWISE);
//
//
//        rubiksCube.front(RotateDirection.COUNTERCLOCKWISE);
//        rubiksCube.front(RotateDirection.CLOCKWISE);
//
//
//        rubiksCube.back(RotateDirection.COUNTERCLOCKWISE);
//        rubiksCube.back(RotateDirection.CLOCKWISE);
//







        System.out.println(rubiksCube);





//        System.out.println(rubiksCube);
    }
}
