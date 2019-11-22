package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.NotesCommand;
import com.balakin.sberbankast.domain.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Nullable
    @Synchronized
    @Override
    public Notes convert(NotesCommand notesCommand) {
        if(notesCommand==null) {
            return null;
        }
        final Notes notes = new Notes();
        notes.setId(notesCommand.getId());
        notes.setDescription(notesCommand.getDescription());

        return notes;
    }
}
