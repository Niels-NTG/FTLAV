package net.blerf.ftl.parser.sectortree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.random.RandRNG;
import net.blerf.ftl.xml.SectorDescription;
import net.blerf.ftl.xml.SectorType;


/**
 * A generator of SectorDots to populate a SectorTree object.
 *
 * FTL calls srand()/rand() from platform-dependent C libraries. A savedgame
 * copied and played on different OSs will yield different random results.
 *
 * The algorithm was derived from disassembling/debugging FTLGame.exe
 * (Humble Bundle v1.5.13), with tacit approval from Matthew Davis.
 *
 * Matthew: "Looks pretty accurate to me. I double checked your
 * probabilities and it all looks to match my code :) Well done!"
 *
 * @see net.blerf.ftl.parser.random.NativeRandom
 */
public class RandomSectorTreeGenerator {

	protected RandRNG rng;


	public RandomSectorTreeGenerator(RandRNG rng) {
		this.rng = rng;
	}


	/**
	 * Sets a new provider for random ints.
	 */
	public void setRNG(RandRNG newRNG) {
		rng = newRNG;
	}

	public RandRNG getRNG() {
		return rng;
	}


	public List<List<SectorDot>> generateSectorTree(int seed, boolean dlcEnabled) {
		List<Sector> civilianSectors = getDatSectors("CIVILIAN", dlcEnabled);
		List<Sector> nebulaSectors = getDatSectors("NEBULA", dlcEnabled);
		List<Sector> hostileSectors = getDatSectors("HOSTILE", dlcEnabled);

		return generateSectorTree(seed, civilianSectors, nebulaSectors, hostileSectors);
	}

	public List<List<SectorDot>> generateTestSectorTree(int seed, boolean preAE, boolean dlcEnabled) {
		// Interesting seed (1.03.3): 19019.
		// Interesting seed (1.5.13 Orig): 29351.
		// Interesting seed (1.5.13 DLC): 22297.

		List<Sector> civilianSectors = getTestCivilianSectors(preAE, dlcEnabled);
		List<Sector> nebulaSectors = getTestNebulaSectors(preAE, dlcEnabled);
		List<Sector> hostileSectors = getTestHostileSectors(preAE, dlcEnabled);

		return generateSectorTree(seed, civilianSectors, nebulaSectors, hostileSectors);
	}

	private List<List<SectorDot>> generateSectorTree(int seed, List<Sector> civilianSectors, List<Sector> nebulaSectors, List<Sector> hostileSectors) {

		rng.srand(seed);

		int n;
		int colSize = 0;
		List<String> existingUniqueIds = new ArrayList<String>();
		List<List<SectorDot>> dotColumns = new ArrayList<List<SectorDot>>(8);

		for (int c=0; c < 8; c++) {
			List<SectorDot> columnDots = new ArrayList<SectorDot>(4);

			if (c == 0) {
				String type = getRandomSectorType();  // Eat a rand() call and disregard the result.

				// Title isn't randomly chosen from sectorDescription's nameList tag!?

				SectorDot dot = new SectorDot("CIVILIAN", "CIVILIAN_SECTOR", "Civilian Sector");
				columnDots.add(dot);
			}
			else if (c == 7) {
				String type = getRandomSectorType();  // Eat a rand() call and disregard the result.

				// Title isn't randomly chosen from sectorDescription's nameList tag!?

				SectorDot dot = new SectorDot("HOSTILE", "FINAL", "The Last Stand");
				columnDots.add(dot);
			}
			else {
				colSize = getRandomSectorColumnSize(c, colSize);

				List<String> sectorTypesInColumn = new ArrayList<String>(4);

				for (int d=0; d < colSize; d++) {
					String sectorTypeId = getRandomSectorType();
					sectorTypesInColumn.add(sectorTypeId);
				}

				for (String sectorTypeId : sectorTypesInColumn) {
					List<Sector> sectorPool = null;
					if ("CIVILIAN".equals(sectorTypeId)) {
						sectorPool = civilianSectors;
					}
					if ("HOSTILE".equals(sectorTypeId)) {
						sectorPool = hostileSectors;
					}
					else if ("NEBULA".equals(sectorTypeId)) {
						sectorPool = nebulaSectors;
					}

					List<Sector> candidates = new ArrayList<Sector>();
					for (Sector s : sectorPool) {
						if (s.min <= c && !existingUniqueIds.contains(s.getId())) {
							candidates.add(s);
						}
					}

					n = rng.rand();
					int candidateIndex = n % candidates.size();
					Sector sector = candidates.get(candidateIndex);
					if (sector.isUnique()) existingUniqueIds.add(sector.getId());

					n = rng.rand();
					String sectorTitle = sector.getTitleList().get(n % sector.getTitleList().size());

					SectorDot dot = new SectorDot(sectorTypeId, sector.getId(), sectorTitle);
					columnDots.add(dot);
				}
			}

			dotColumns.add(columnDots);
		}

		return dotColumns;
	}


	/**
	 * Returns a random number from 2 to 4 (inclusive).
	 *
	 * When column == 1, the result will always be 2.
	 * Otherwise, the result will be different from prevSize.
	 */
	protected int getRandomSectorColumnSize(int column, int prevSize) {
		int result = 0;
		do {
			int n = rng.rand();
			result = n % 3 + 2;
		} while (result == prevSize);

		// Second column always has two dots.
		if (column == 1) result = 2;

		return result;
	}

	/**
	 * Returns a random SectorType id: NEBULA, HOSTILE, CIVILIAN.
	 */
	protected String getRandomSectorType() {
		String result = null;
		int n = rng.rand();

		if (n % 10 <= 1) {
			result = "NEBULA";    // 2 in FTLGame.exe.
		}
		else if (n % 10 > 5) {
			result = "HOSTILE";   // 1 in FTLGame.exe.
		}
		else {
			result = "CIVILIAN";  // 0 in FTLGame.exe.
		}

		return result;
	}


	protected List<Sector> getDatSectors(String sectorTypeId, boolean dlcEnabled) {
		List<Sector> result = new ArrayList<Sector>();
		SectorType tmpType = DataManager.getInstance().getSectorTypeById(sectorTypeId, dlcEnabled);

		for (String sectorId : tmpType.getSectorIds()) {
			SectorDescription tmpDesc = DataManager.getInstance().getSectorDescriptionById(sectorId);

			Sector tmpSector = new Sector(tmpDesc.isUnique(), tmpDesc.getMinSector(), tmpDesc.getId(), new ArrayList<String>(tmpDesc.getNameList().names));
			result.add(tmpSector);
		}

		return result;
	}

	private List<Sector> getTestCivilianSectors(boolean preAE, boolean dlcEnabled) {
		List<Sector> civilianSectors = new ArrayList<Sector>();
		civilianSectors.add(new Sector(false, 0, "CIVILIAN_SECTOR", Arrays.asList("Civilian Sector")));
		civilianSectors.add(new Sector(false, 0, "ENGI_SECTOR", Arrays.asList("Engi Controlled Sector")));
		civilianSectors.add(new Sector(true, 2, "ENGI_HOME", Arrays.asList("Engi Homeworlds")));
		civilianSectors.add(new Sector(false, 0, "ZOLTAN_SECTOR", Arrays.asList("Zoltan Controlled Sector")));
		civilianSectors.add(new Sector(true, 1, "ZOLTAN_HOME", Arrays.asList("Zoltan Homeworlds")));

		if (preAE) {  // 1.03.3.
			for (Iterator<Sector> it=civilianSectors.iterator(); it.hasNext();) {
				Sector s = it.next();
				if ("ZOLTAN_SECTOR".equals(s.getId())) {
					s.min = 1;
				}
				else if ("ZOLTAN_HOME".equals(s.getId())) {
					s.min = 2;
				}
			}
		}

		return civilianSectors;
	}

	private List<Sector> getTestNebulaSectors(boolean preAE, boolean dlcEnabled) {
		List<Sector> nebulaSectors = new ArrayList<Sector>();
		nebulaSectors.add(new Sector(false, 0, "NEBULA_SECTOR", Arrays.asList("Uncharted Nebula")));
		nebulaSectors.add(new Sector(true, 3, "SLUG_HOME", Arrays.asList("Slug Home Nebula")));
		nebulaSectors.add(new Sector(false, 3, "SLUG_SECTOR", Arrays.asList("Slug Controlled Nebula")));

		return nebulaSectors;
	}

	private List<Sector> getTestHostileSectors(boolean preAE, boolean dlcEnabled) {
		List<Sector> hostileSectors = new ArrayList<Sector>();
		hostileSectors.add(new Sector(false, 0, "REBEL_SECTOR", Arrays.asList("Rebel Controlled Sector")));
		hostileSectors.add(new Sector(true, 4, "REBEL_SECTOR_MINIBOSS", Arrays.asList("Rebel Stronghold")));
		hostileSectors.add(new Sector(false, 0, "PIRATE_SECTOR", Arrays.asList("Pirate Controlled Sector")));
		hostileSectors.add(new Sector(false, 0, "MANTIS_SECTOR", Arrays.asList("Mantis Controlled Sector")));
		hostileSectors.add(new Sector(true, 2, "MANTIS_HOME", Arrays.asList("Mantis Homeworlds")));
		hostileSectors.add(new Sector(false, 1, "ROCK_SECTOR", Arrays.asList("Rock Controlled Sector")));
		hostileSectors.add(new Sector(true, 4, "ROCK_HOME", Arrays.asList("Rock Homeworlds")));
		hostileSectors.add(new Sector(false, 1, "LANIUS_SECTOR", Arrays.asList("Abandoned Sector")));

		if (preAE) {  // 1.03.3.
			for (Iterator<Sector> it=hostileSectors.iterator(); it.hasNext();) {
				Sector s = it.next();
				if ("REBEL_SECTOR_MINIBOSS".equals(s.getId()) || "LANIUS_SECTOR".equals(s.getId())) {
					it.remove();
				}
			}
		}
		else if (!dlcEnabled) {
			for (Iterator<Sector> it=hostileSectors.iterator(); it.hasNext();) {
				Sector s = it.next();
				if ("LANIUS_SECTOR".equals(s.getId())) {
					it.remove();
				}
			}
		}

		return hostileSectors;
	}



	protected static class Sector {
		private final boolean unique;
		private final String id;
		private final List<String> titleList;
                private int min;

		public Sector(boolean unique, int min, String id, List<String> titleList) {
			this.unique = unique;
			this.min = min;
			this.id = id;
			this.titleList = titleList;
		}

		public boolean isUnique() { return unique; }
		public int getMin() { return min; }
		public String getId() { return id; }
		public List<String> getTitleList() { return titleList; }
	}
}
