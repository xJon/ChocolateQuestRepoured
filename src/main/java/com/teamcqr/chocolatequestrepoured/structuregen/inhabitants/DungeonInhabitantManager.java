package com.teamcqr.chocolatequestrepoured.structuregen.inhabitants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DungeonInhabitantManager {

	private Map<String, DungeonInhabitant> inhabitantMapping = new HashMap<>();
	// ATTENTION: the entry "default" (or similar) is NEVER allowed to be put in the distant mapping!!
	private List<List<String>> distantMapping = new ArrayList<>();
	private Random random = new Random();

	private static DungeonInhabitantManager INSTANCE;

	public static final String DEFAULT_INHABITANT_IDENT = "DEFAULT";

	private DungeonInhabitantManager() {
		this.loadDefaultInhabitants();
		this.loadInhabitantConfigs();
		this.loadDistantMapping();
	}

	private void loadDefaultInhabitants() {
		for (EDefaultInhabitants defInha : EDefaultInhabitants.values()) {
			DungeonInhabitant inha = new DungeonInhabitant(defInha);
			this.inhabitantMapping.put(inha.getName().toUpperCase(), inha);
		}
	}

	private void loadInhabitantConfigs() {
		List<File> files = new ArrayList<>(FileUtils.listFiles(CQRMain.CQ_INHABITANT_FOLDER, new String[] { "cfg", "prop", "properties" }, true));
		int fileCount = files.size();
		if (fileCount > 0) {
			boolean flag = true;
			for (int i = 0; i < fileCount; i++) {
				File file = files.get(i);
				Properties prop = new Properties();
				try (InputStream inputStream = new FileInputStream(file)) {
					prop.load(inputStream);
					flag = true;
				} catch (IOException e) {
					CQRMain.logger.error("Failed to load file" + file.getName(), e);
					flag = false;
					continue;
				}
				if (flag) {
					try {
						DungeonInhabitant inha = new DungeonInhabitant(prop);
						this.inhabitantMapping.put(inha.getName().toUpperCase(), inha);
					} catch (Exception ex) {
						CQRMain.logger.warn("Failed to create DungeonInhabitant object from file: " + file.getName());
					}
				}
			}
		}
	}

	private void loadDistantMapping() {
		File file = new File(CQRMain.CQ_CONFIG_FOLDER, "defaultInhabitantConfig.properties");
		if (file.exists()) {
			FileReader reader;
			try {
				reader = new FileReader(file);
				BufferedReader br = new BufferedReader(reader);
				String currentLine;

				while ((currentLine = br.readLine()) != null) {
					if (currentLine.startsWith("#")) {
						continue;
					}
					String[] entries = currentLine.split(",");
					List<String> tmpList = new ArrayList<>();
					for (String s : entries) {
						s = s.trim();
						if (this.inhabitantMapping.containsKey(s) && !s.equalsIgnoreCase(DEFAULT_INHABITANT_IDENT)) {
							tmpList.add(s);
						}
					}
					if (!tmpList.isEmpty()) {
						this.distantMapping.add(tmpList);
					}
				}
				reader.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// System.out.println(distantMapping.toString());
			// System.out.println("LOADED!");
		}
	}

	public static void init() {
		if (INSTANCE == null) {
			INSTANCE = new DungeonInhabitantManager();
		}
	}

	public static boolean isValidInhabitant(String name) {
		if (INSTANCE == null) {
			INSTANCE = new DungeonInhabitantManager();
		}
		return INSTANCE.isValid(name);
	}

	private boolean isValid(String name) {
		return name.equalsIgnoreCase(DEFAULT_INHABITANT_IDENT) || this.inhabitantMapping.containsKey(name);
	}

	public static DungeonInhabitant getInhabitantByName(String name) {
		if (INSTANCE == null) {
			INSTANCE = new DungeonInhabitantManager();
		}
		return INSTANCE.getInhabitant(name);
	}

	public static DungeonInhabitant getInhabitantDependingOnDistance(World world, int blockX, int blockZ) {
		if (INSTANCE == null) {
			INSTANCE = new DungeonInhabitantManager();
		}
		return INSTANCE.getInhabitantByDistance(world, blockX, blockZ);
	}

	private DungeonInhabitant getInhabitantByDistance(World world, int blockX, int blockZ) {
		if (this.distantMapping.isEmpty()) {
			return (DungeonInhabitant) this.inhabitantMapping.values().toArray()[this.random.nextInt(this.inhabitantMapping.values().size())];
		}
		BlockPos spawnPoint = world.getSpawnPoint();
		int x1 = blockX - spawnPoint.getX();
		int z1 = blockZ - spawnPoint.getZ();
		int distToSpawn = (int) Math.sqrt((double) (x1 * x1 + z1 * z1));
		int index = distToSpawn / CQRConfig.mobs.mobTypeChangeDistance;

		if (index >= this.distantMapping.size()) {
			index = this.random.nextInt(this.distantMapping.size());
		}
		List<String> tmpList = this.distantMapping.get(index);
		return this.getInhabitant(tmpList.get(this.random.nextInt(tmpList.size())));

	}

	public DungeonInhabitant getInhabitant(String name) {
		if (name.equalsIgnoreCase(DEFAULT_INHABITANT_IDENT) || !this.inhabitantMapping.containsKey(name)) {
			List<String> tmpList = this.distantMapping.get(this.random.nextInt(this.distantMapping.size()));
			return this.getInhabitant(tmpList.get(this.random.nextInt(tmpList.size())));
		}
		return this.inhabitantMapping.getOrDefault(name, this.inhabitantMapping.get("ILLAGER"));
	}

	public static List<DungeonInhabitant> getAllInhabitantsFromFaction(CQRFaction faction, World world) {
		if (INSTANCE == null) {
			INSTANCE = new DungeonInhabitantManager();
		}

		return INSTANCE.getListOfFactionInhabitants(faction, world);
	}

	private List<DungeonInhabitant> getListOfFactionInhabitants(CQRFaction faction, World world) {
		ArrayList<DungeonInhabitant> result = new ArrayList<>();

		for (DungeonInhabitant inha : this.inhabitantMapping.values()) {
			if (!inha.getName().equalsIgnoreCase(DEFAULT_INHABITANT_IDENT)) {
				if (inha.getFactionOverride() != null) {
					if (FactionRegistry.instance().getFactionInstance(inha.getFactionOverride()).equals(faction)) {
						result.add(inha);
					}
				} else {
					Entity entity = EntityList.createEntityByIDFromName(inha.getEntityID(), world);
					if (entity != null) {
						if (FactionRegistry.instance().getFactionOf(entity).equals(faction)) {
							result.add(inha);
						}

					}
				}
			}
		}

		return result;
	}

}
