import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.slf4j.Logger;

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
    }

    private static void handleMove(Context ctx) {
        log.info(ctx.body());

        Move move = ctx.bodyAsClass(Move.class);
        boolean isValidMove = Main.board.isPlaceValid(move);
        if(!isValidMove) {
            ctx.status(400);
            ctx.status(200);
            ctx.json(getMockBoard());
        }else{
            Main.board.doMove(move);
            ctx.status(200);
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