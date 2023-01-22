package org.icepdf.core.util.updater.writeables;

import org.icepdf.core.io.CountingOutputStream;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.DictionaryEntries;
import org.icepdf.core.pobjects.PTrailer;

import java.io.IOException;
import java.util.List;

public class TrailerWriter extends BaseTableWriter {

    public void writeTrailer(PTrailer prevTrailer, long xrefPosition, List<Entry> entries, CountingOutputStream output)
            throws IOException {
        DictionaryEntries newTrailer = (DictionaryEntries) prevTrailer.getDictionary().clone();
        long previousTrailerPosition = this.setPreviousTrailer(newTrailer, prevTrailer);
        this.setTrailerSize(newTrailer, prevTrailer, entries);
        newTrailer.remove(PTrailer.XREF_STRM_KEY);

        if (previousTrailerPosition == 0) {
            throw new IllegalStateException("Cannot write trailer to an PDF with an invalid object offset");
        }

        output.write(TRAILER);
        this.writeDictionary(new Dictionary(null, newTrailer), output);
        output.write(STARTXREF);
        this.writeLong(xrefPosition, output);
        output.write(NEWLINE);
        output.write(COMMENT_EOF);
    }
}
