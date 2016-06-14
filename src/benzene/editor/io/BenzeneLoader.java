package benzene.editor.io;

import benzene.editor.Benzene;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author nvcleemp
 */
public class BenzeneLoader {
    
    private final Benzene benzene;

    public BenzeneLoader(Benzene benzene) {
        this.benzene = benzene;
    }
    
    /**
     * Loads the benzenoid from the given file.
     * 
     * @param f The file from which the benzenoid should be read
     * @return true if loading was successful and false otherwise
     */
    public boolean load(File f){
        try {
            JAXBContext jc = JAXBContext.newInstance(BenzeneJaxbDelegate.class);
            BenzeneJaxbDelegate delegate = (BenzeneJaxbDelegate)jc.createUnmarshaller()
                    .unmarshal(f);
            benzene.set(delegate.getHexagons());
        } catch (JAXBException ex) {
            Logger.getLogger(BenzeneLoader.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
