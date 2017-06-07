import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = -6112428091888191314L;

    private boolean isRunnung = false;
    private Thread thread;
    private Handler handler;
    private BufferedImage level = null;
    private Camera camera;

    public Game() {
        new Window(1000, 563, "Wizard Wars", this);
        start();
        handler = new Handler();
        camera = new Camera(0, 0);
        this.addKeyListener(new KeyInput(handler));
        this.addMouseListener(new MouseInput(handler, camera));

        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/wizard_level.png");
        loadLevel(level);

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
        Graphics2D g2d = (Graphics2D) g;
        //////////////////////////////////////////////////////////

        g.setColor(Color.red);
        g.fillRect(0, 0, 1000, 563);

        g2d.translate(-camera.getX(), -camera.getY());



        handler.render(g);

        //////////////////////////////////////////////////////////
        g.dispose();
        bufferStrategy.show();
    }

    //loading the level
    private void loadLevel(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int xx = 0; xx < width; xx++) {
            for(int yy = 0; yy < height; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if(red == 255) {
                    handler.addObject(new Block(xx * 32, yy * 32, ID.Block, handler));
                }

                if(blue == 255) {
                    handler.addObject(new Wizard(xx*32, yy*32, ID.Player, handler));
                }
            }
        }
    }

    private void tick() {

        for(int i = 0; i < handler.objects.size(); i++) {
            if(handler.objects.get(i).getId() == ID.Player) {
                camera.tick(handler.objects.get(i));
            }
        }

        handler.tick();
    }

    public static void main(String args []) {
        new Game();
    }
}
