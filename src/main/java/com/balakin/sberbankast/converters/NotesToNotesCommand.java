package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.NotesCommand;
import com.balakin.sberbankast.domain.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {

    @Nullable
    @Synchronized
    @Override
    public NotesCommand convert(Notes notes) {

        if(notes==null){
            return null;
        }
        final NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(notes.getId());
        notesCommand.setDescription(notes.getDescription());
        return notesCommand;
    }
}
