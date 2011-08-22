package miner.sparql;

import java.util.*;

import miner.util.*;


public class ResultPairsIterator implements Iterator<String[]> {

	private QueryEngine m_engine;
	
	private String m_sQuery;
	
	private int m_iMaxChunkSize;
	
	private List<String[]> m_nextChunk;
	
	private int iNext = 0;
	
	private int m_iOffset = 0;
	
	private String filter;
	
	
	protected ResultPairsIterator( QueryEngine engine, String sQuery, String filter ){
		m_sQuery = sQuery;
		m_engine = engine;
		m_nextChunk = new ArrayList<String[]>();
		m_iMaxChunkSize = m_engine.getChunkSize();
		this.filter = filter;
	}
	
	public boolean hasNext() {
		int iChunkSize = m_nextChunk.size();
		if( iNext < iChunkSize ){
			return true;
		}
		else if( iChunkSize > 0 && iChunkSize < m_iMaxChunkSize ){
			return false;
		}
		try {
			m_nextChunk = m_engine.execute( m_sQuery +" LIMIT "+ m_iMaxChunkSize +" OFFSET "+ m_iOffset, "x", "y", filter );
			m_iOffset += m_nextChunk.size();
			iNext = 0;
			return m_nextChunk.size() > 0;
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String[] next(){
		return m_nextChunk.get( iNext++ );
	}

	public void remove(){}
}

