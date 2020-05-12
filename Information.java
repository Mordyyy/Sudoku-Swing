public class Information {
    private String metropolis, continent;
    private int population;

    public Information(String metropolis, String continent, int population) {
        this.metropolis = metropolis;
        this.continent = continent;
        this.population = population;
    }

    public String getMetropolis() {
        return metropolis;
    }

    public String getContinent() {
        return continent;
    }

    public int getPopulation() {
        return population;
    }
}

