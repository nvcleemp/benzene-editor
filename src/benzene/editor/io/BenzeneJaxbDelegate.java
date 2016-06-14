package benzene.editor.io;

import benzene.editor.utils.Location;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nvcleemp
 */
@XmlRootElement(name="benzene")
public class BenzeneJaxbDelegate {
    
    private List<Location> hexagons = new ArrayList<>();

    @XmlElement(name="hexagon")
    public List<Location> getHexagons() {
        return hexagons;
    }

    public void setHexagons(List<Location> hexagons) {
        this.hexagons = hexagons;
    }
}
