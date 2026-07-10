public class Celula {

    private boolean parede;

    private Caixa caixa;

    private Dinossauro dinossauro;

    public Celula() {

        parede = false;
        caixa = null;
        dinossauro = null;
    }

    public boolean isParede() {
        return parede;
    }

    public void setParede(boolean parede) {
        this.parede = parede;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public Dinossauro getDinossauro() {
        return dinossauro;
    }

    public void setDinossauro(Dinossauro dinossauro) {
        this.dinossauro = dinossauro;
    }
}