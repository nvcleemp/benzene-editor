package benzene.editor.io;

import benzene.editor.Benzene;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author nvcleemp
 */
public class BenzeneSaver {
    
    private final Benzene benzene;
    private final BenzeneJaxbDelegate delegate;

    public BenzeneSaver(Benzene benzene) {
        this.benzene = benzene;
        delegate = new BenzeneJaxbDelegate();
    }
    
    /**
     * Saves the benzenoid to the given file.
     * 
     * @param f The file to which the benzenoid should be written
     * @return true if saving was successful and false otherwise
     */
    public boolean save(File f){
        delegate.setHexagons(benzene.locations().collect(Collectors.toList()));
        try {
            JAXBContext jc = JAXBContext.newInstance(BenzeneJaxbDelegate.class);
            jc.createMarshaller().marshal(delegate, new FileWriter(f));
        } catch (JAXBException | IOException ex) {
            Logger.getLogger(BenzeneSaver.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
