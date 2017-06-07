import java.awt.*;

public class Bullet extends GameObject{



    public Bullet(int x, int y, ID id, Handler handler, int mx, int my) {
        super(x, y, id, handler);

        velX = (mx - x) / 10;
        velY = (my - y) / 10;
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);

            if(tempObject.getId() == ID.Block) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.objects.remove(this);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.green);
        g.fillOval(x, y, 8, 8);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 8, 8);
    }
}
