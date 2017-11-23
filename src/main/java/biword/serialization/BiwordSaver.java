package biword.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import algorithm.BiwordedStructure;
import global.io.Directories;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import global.Parameters;

/**
 * @author Antonin Pavelka
 */
public class BiwordSaver {

	private final Parameters parameters;
	private final Directories dirs;
	private final KryoFactory kryoFactory = new KryoFactory();

	public BiwordSaver(Parameters parameters, Directories dirs) {
		this.parameters = parameters;
		this.dirs = dirs;
	}

	public void save(int structureId, BiwordedStructure bws) {
		save(bws, dirs.getBiwordedStructure(structureId));
	}

	private void save(BiwordedStructure o, File f) {
		try (Output output = new Output(new FileOutputStream(f))) {
			Kryo kryo = kryoFactory.getKryo();
			kryo.writeObject(output, o);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
