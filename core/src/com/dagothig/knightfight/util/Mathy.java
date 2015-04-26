package com.dagothig.knightfight.util;

import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.entity.Cylinder;

/**
 * Created by dagothig on 4/26/15.
 */
public class Mathy {
    public static Float timeToCollision(Cylinder c1, Cylinder c2, Vector3 vel) {
        /*
        On pose r3 = r1² + r2²
        xd = x1 - x2
        yd = y1 - y2
        On cherche moment où la distance entre les deux cercles est égale à 0,
        ou plutôt: d(r1, r2) = d((x1, y1) + (vx, vt)t - (x2, y2)), résolut pour t
        => r1² + r2² = (x1 + vx*t - x2)² + (y1 + vy*t - y2)²
        <=> r3 = (xd + vx*t)² + (yd + vy*t)²
        (où toutes les variables \ {t} sont des constantes)
        En factorisant, on arrive à la forme
        0 = (vx² + vy²)t² + (2*xd*vx + 2*yd*vy)t + xd² + yd² - r3
        Étant donné l'équation quadratique
        0 = ax² + bx + c
        on a que
        t = (-b +/-racine(b² - 4ac)) / 2a
        => a = vx² + vy², b = 2*xd*vx + 2*yd*vy, c = xd² + yd² - r3
        */
        float r3 = c1.radiusSquared + c2.radiusSquared;
        float xd = c1.pos.x - c2.pos.x;
        float yd = c1.pos.y - c2.pos.y;
        float a = vel.x * vel.x + vel.y * vel.y;
        float b = 2 * xd * vel.x + 2 * yd * vel.y;
        float c = xd * xd + yd * yd - r3;
        if (a == 0) return null;
        float discriminantSquared = b * b - 4 * a * c;
        if (discriminantSquared < 0) return null;
        float discriminant = (float)Math.sqrt(discriminantSquared);
        float t1 = (-b + discriminant) / (2 * a);
        float t2 = (-b - discriminant) / (2 * a);
        return Math.min(t1, t2);
    }

    public static float angleOf(Vector3 vec) {
        return (float)Math.atan2(vec.y, vec.x);
    }

    public static float angleAtCollision(Cylinder c1, Cylinder c2, Vector3 vel) {
        Vector3 velPos = VectorPool.V3();
        velPos.set(c1.pos).add(vel);
        float angle = (float)Math.atan2(velPos.y - c2.pos.y, velPos.x - c2.pos.x);
        VectorPool.claim(velPos);
        return angle;
    }


}
