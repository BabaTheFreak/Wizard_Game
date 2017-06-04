import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = -6112428091888191314L;

    private boolean isRunnung = false;
    private Thread thread;
    private Handler handler;

    public Game() {
        new Window(1000, 563, "Wizard Game", this);
        start();
        handler = new Handler();

        handler.addObject(new Box(100, 100));
    }

    private void start() {
        isRunnung = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunnung = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isRunnung) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }

    private void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bufferStrategy.getDrawGraphics();
        //////////////////////////////////////////////////////////

        g.setColor(Color.red);
        g.fillRect(0, 0, 1000, 563);

        handler.render(g);

        //////////////////////////////////////////////////////////
        g.dispose();
        bufferStrategy.show();
    }

    private void tick() {
        handler.tick();
    }

    public static void main(String args []) {
        new Game();
    }
}
