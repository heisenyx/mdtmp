import './PublicationPage.css'

import { useParams } from 'react-router-dom';
import { getPublication } from '../services/client';
import { useEffect, useState } from 'react';
import { useEditor, EditorContent } from '@tiptap/react'
import { StarterKit } from '@syfxlin/tiptap-starter-kit'
import { Markdown } from 'tiptap-markdown';

export default function PublicationPage() {
    const { hash } = useParams();
    const [publication, setPublication] = useState();

    useEffect(() => {
        async function fetchPublication() {
            try {
                const response = await getPublication(hash);
                setPublication(response.data);
            } catch (e) {
                console.error("Failed to fetch publication:", e);
            }
        }
        fetchPublication();
    }, [hash]);

    const editor = useEditor({
        extensions: [StarterKit, Markdown],
        editable: false
    });

    if (!publication) {
        return;
    } else {
        editor.commands.setContent(publication.content);
    }

    return (
        <>
            <div className="editor-container">
                <p className='input-field'>{publication.title}</p>
                <p className='input-field author'>{publication.author || 'Anonymous'}</p>

                <EditorContent editor={editor} />
            </div>
        </>
    )
}