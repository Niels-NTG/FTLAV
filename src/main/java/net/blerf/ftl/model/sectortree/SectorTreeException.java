package net.blerf.ftl.model.sectortree;


public class SectorTreeException extends Exception {
	protected int column = -1;
	protected int row = -1;

	public SectorTreeException( String message ) {
		super( message );
	}
	public SectorTreeException( Throwable throwable ) {
		super( throwable );
	}
	public SectorTreeException( String message, Throwable throwable ) {
		super( message, throwable );
	}

	public void setColumn( int n ) { column = n; }
	public int getColumn() { return column; }

	public void setRow( int n ) { row = n; }
	public int getRow() { return row; }
}
