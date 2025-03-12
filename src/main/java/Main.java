import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.slf4j.Logger;

import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    private static final int BOARD_SIZE = 8;
    private static Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final Board board = new Board();

    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.http.asyncTimeout = 10_000L;
//                config.plugins.enableCors(cors -> {
//                    cors.add(it -> {
//                        it.allowHost("http://localhost:5173");
//                    });
//                });
            config.bundledPlugins.enableCors(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost));
            config.router.apiBuilder(() -> {
                get("/path", ctx -> ctx.result("Hello, World!"));
                get("/board", ctx -> ctx.json(getMockBoard()));
                post("/move", ctx -> {
                            handleMove(ctx);

                        }
                );
                delete("board", ctx -> {
                    ctx.status(200);
                });
            });
        }).start(7070);
        doTest();

    }

    private static void doTest(){
        board.doMove(new Move(0, 0, Colour.BLACK.getValue()));
        board.doMove(new Move(7, 7, Colour.BLACK.getValue()));
        for (int i = 1; i < 7; i++) {
            board.doMove(new Move(0, i, Colour.WHITE.getValue()));
        }
        for (int i = 1; i < 7; i++) {
            board.doMove(new Move(i, 7, Colour.WHITE.getValue()));
        }
//        int len = board.walkTheRotation(new Move(0, 7, Colour.BLACK.getValue()), 0, -1, 1);
        List<Rotation> rotations = board.getValidRotations(new Move(0, 7, Colour.BLACK.getValue()));
        System.out.println(rotations);
    }

    private static void handleMove(Context ctx) {
        log.info(ctx.body());

        Move move = ctx.bodyAsClass(Move.class);
        boolean isValidMove = Main.board.isPlaceValid(move);
        if(isValidMove) {
            ctx.status(200);
            Main.board.doMove(move);
            ctx.json(getMockBoard());
        }else{
            ctx.status(400);
            ctx.json(getMockBoard());
        }

//        Main.board[move.getRow() + 1][move.getColumn() + 1] = 2;
//        ctx.json("OK");
//        ctx.status(200);
    }

    private static int[][] getMockBoard() {
        return board.getBoardState();
    }
}